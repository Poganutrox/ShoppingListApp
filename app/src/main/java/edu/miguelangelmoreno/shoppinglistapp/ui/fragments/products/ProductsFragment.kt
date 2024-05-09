package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
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
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentProductsBinding
import edu.miguelangelmoreno.shoppinglistapp.utils.checkConnection
import edu.miguelangelmoreno.shoppinglistapp.utils.makeToast
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
        binding.swipeRefresh.setOnRefreshListener {
            getProducts(true)
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

    private fun getProducts(isRefreshing: Boolean = false) {
        if (!checkConnection(requireContext())) {
            makeToast(requireContext(), "No connection")
        } else {
            if (isRefreshing) {
                vm.setFilters(null, null, null, false, null, null)
            } else {
                setFilters()
            }

            lifecycleScope.launch {
                vm.productList.collect {
                    adapter.submitData(it)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

    }

    private fun setFilters() {
        val productName: String? = if (args.productName.isNullOrEmpty()) null else args.productName
        val categoryId: Int? = if (args.categoryId == -1) null else args.categoryId
        val alphabeticSort: Int? = if (args.alphabeticSort == -1) null else args.alphabeticSort
        val priceSort: Int? = if (args.priceSort == -1) null else args.priceSort
        val onSale = args.onSale
        val supermarketIds = mutableSetOf<Int>()
        if (args.mercadonaId != -1) supermarketIds.add(args.mercadonaId)
        if (args.diaId != -1) supermarketIds.add(args.diaId)
        if (args.consumId != -1) supermarketIds.add(args.consumId)
        if (args.alcampoId != -1) supermarketIds.add(args.alcampoId)

        vm.setFilters(productName, categoryId, supermarketIds, onSale, alphabeticSort, priceSort)
    }

    private fun initRecyclerView() {
        adapter = ProductListAdapter(
            onClickAskQuantity = { productId, position ->
                if (addPrefs.isAdding()) {
                    showQuantityDialog(productId, position)
                }
            },
            onClickFav = { productId, position ->
                vm.likeProduct(productId)
                adapter.notifyItemChanged(position)
            },
            onLongClickDetailView = { productId ->
                findNavController().navigate(
                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                        productId = productId
                    )
                )
            }
        )
        binding.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerProducts.adapter = adapter
    }

    private fun showQuantityDialog(productId: String, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Añadir cantidad")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { _, _ ->
            val quantity = input.text.toString().toIntOrNull()
            if (quantity == null) {
                addPrefs.addProductToList(productId, 0)
                adapter.notifyItemChanged(position)
            } else if (quantity >= 0) {
                addPrefs.addProductToList(productId, quantity)
                adapter.notifyItemChanged(position)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

}
