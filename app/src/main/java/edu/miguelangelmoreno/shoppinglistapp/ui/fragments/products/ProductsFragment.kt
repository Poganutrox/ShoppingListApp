package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.filterPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.DialogAddProductBinding
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentProductsBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.Supermarkets
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity
import edu.miguelangelmoreno.shoppinglistapp.utils.checkConnection
import edu.miguelangelmoreno.shoppinglistapp.utils.makeToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private val vm: ProductsViewModel by viewModels()
    private lateinit var adapter: ProductListAdapter
    private val args: ProductsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListeners()
        observeProductState()
        getProducts()
    }

    private fun initListeners() {
        binding.fabFilter.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_filtersFragment)
        }
    }

    private fun observeProductState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                }
            }
        }
    }

    private fun getProducts() {
        if (!checkConnection(requireContext())) {
            makeToast(requireContext(), "No connection, using local storage")
        }
        setFilters()

        lifecycleScope.launch {
            vm.productList.collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setFilters(){
        var productName: String? = if (args.productName.isNullOrEmpty()) null else args.productName
        var categoryId: Int? = if (args.categoryId == -1) null else args.categoryId
        val onSale = args.onSale
        val supermarketIds = mutableSetOf<Int>()
        if (args.mercadonaId != -1) supermarketIds.add(args.mercadonaId)
        if (args.diaId != -1) supermarketIds.add(args.diaId)
        if (args.consumId != -1) supermarketIds.add(args.consumId)
        if (args.alcampoId != -1) supermarketIds.add(args.alcampoId)

        vm.setFilters(productName, categoryId, supermarketIds, onSale)
    }
    private fun initRecyclerView() {
        adapter = ProductListAdapter(
            onClickFav = { productId, btnFav ->
                vm.likeProduct(productId)
                btnFav.setImageState(intArrayOf(R.attr.state_fav), vm.productState.value.isFavourite)
            },
            onClickDetailView = { productId ->
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                    productId = productId
                ))
            })
        binding.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerProducts.adapter = adapter
    }
}
