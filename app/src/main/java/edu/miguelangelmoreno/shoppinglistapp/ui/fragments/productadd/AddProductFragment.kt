package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.productadd

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentAddProductBinding
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListDTO
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListProduct
import edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists.ChildAdapter
import edu.miguelangelmoreno.shoppinglistapp.utils.makeToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private val vm: AddProductViewModel by viewModels()
    private val args: AddProductFragmentArgs by navArgs()
    private lateinit var adapter: ChildAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        vm.getShoppingListProducts()
        initListeners()
        initRecycler()
        observeListChanges()
        observeStateChanges()
    }

    private fun initUI() {
        if (!args.name.isNullOrEmpty()) {
            binding.tvListTitle.setText(args.name)
        }

        if (args.id != null && args.id != -1) {
            binding.btnSaveList.text = getString(R.string.modify_list)
        } else {
            binding.btnSaveList.text = getString(R.string.add_list)
        }
    }

    private fun observeListChanges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.shoppingProductList.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun observeStateChanges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.addProductState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    if (state.isReceived) {
                        if (state.isSuccess) {
                            if (args.id != null && args.id != -1) {
                                makeToast(
                                    requireContext(),
                                    getString(R.string.shopping_list_modified)
                                )
                            } else {
                                makeToast(
                                    requireContext(),
                                    getString(R.string.shopping_list_created)
                                )
                            }

                            addPrefs.setIsAdding(false)
                            addPrefs.clear()
                            findNavController().navigate(R.id.homeFragment)
                        } else {
                            makeToast(
                                requireContext(),
                                getString(R.string.error_creating_shopping_list)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler() {
        adapter = ChildAdapter(
            onClickDelete = { productId, _ ->
                addPrefs.removeProduct(productId)
                vm.getShoppingListProducts()
            }
        )
        binding.productList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.productList.adapter = adapter
    }

    private fun initListeners() {
        binding.btnAddItem.setOnClickListener {
            addPrefs.setIsAdding(true)
            findNavController().navigate(R.id.action_addProductFragment_to_productsFragment)
        }

        binding.btnSaveList.setOnClickListener {
            val id = args.id
            val name = binding.tvListTitle.text.toString()
            val creatorUser = userPrefs.getUserId()
            val shoppingListProduct = addPrefs.getAllProductList()

            if (name.isNullOrEmpty()) {
                binding.tvListTitle.error = getString(R.string.error_list_name)
            } else if (shoppingListProduct.isNullOrEmpty()) {
                makeToast(requireContext(), getString(R.string.error_empty_shopping_list))
            } else {
                val shoppingListDTO =
                    ShoppingListDTO(
                        id,
                        name,
                        creatorUser!!,
                        true,
                        shoppingListProduct,
                        listOf(creatorUser)
                    )
                vm.saveShoppingList(shoppingListDTO)
            }
        }
    }
}