package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentShoppingListsBinding
import edu.miguelangelmoreno.shoppinglistapp.ui.fragments.productadd.AddProductFragmentDirections
import edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products.ProductsFragmentDirections
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingListsFragment : Fragment() {
    private lateinit var  binding : FragmentShoppingListsBinding
    private lateinit var mainAdapter: MainAdapter
    private val vm : ShoppingListsViewModel by viewModels ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecycler()
        observeListChanges()
    }

    private fun initListeners() {
        binding.fabAddList.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addProductFragment)
        }
    }

    private fun observeListChanges(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                vm.shoppingLists.collect{
                    mainAdapter.submitList(it)
                }
            }
        }
    }

    private fun initRecycler(){
        mainAdapter = MainAdapter(
            onClickEdit = {
                addPrefs.clear()
                addPrefs.setIsAdding(true)
                addPrefs.setProductList(it)
                findNavController().navigate(ShoppingListsFragmentDirections.actionHomeFragmentToAddProductFragment(
                    id = it.id!!,
                    name = it.name!!
                ))
            }
        )
        binding.mainRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.mainRecyclerView.adapter = mainAdapter
    }
}