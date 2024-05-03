package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentFavouritesBinding
import edu.miguelangelmoreno.shoppinglistapp.utils.checkConnection
import edu.miguelangelmoreno.shoppinglistapp.utils.makeToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private lateinit var adapter: FavouritesAdapter
    private lateinit var binding: FragmentFavouritesBinding
    private val vm: FavouritesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListeners()
        observeState()
        getProducts()
    }

    private fun observeState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                vm.favouriteState.collect{
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
    }

    private fun getProducts() {
        if (!checkConnection(requireContext())) {
            makeToast(requireContext(), "No connection, using local storage")
        }else{
            lifecycleScope.launch {
                vm.getFavouriteList()

                repeatOnLifecycle(Lifecycle.State.CREATED){
                    vm.favouriteList.collect {
                        adapter.submitList(it)
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }


    }

    private fun initListeners(){
        binding.swipeRefresh.setOnRefreshListener {
            getProducts()
        }
    }

    private fun initRecyclerView() {
        adapter = FavouritesAdapter(
            onClickFav = { productId ->
                vm.likeProduct(productId)
            },
            onLongClickDetailView = { productId ->
                findNavController().navigate(
                    FavouritesFragmentDirections.actionFavouritesFragmentToProductDetailsFragment(
                        productId = productId
                    )
                )
            })
        binding.recyclerFavourites.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerFavourites.adapter = adapter
    }
}