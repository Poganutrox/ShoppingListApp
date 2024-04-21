package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.DialogAddProductBinding
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentProductsBinding
import edu.miguelangelmoreno.shoppinglistapp.utils.checkConnection
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
        getProducts()
    }

    private fun initListeners() {
        binding.fabFilter.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_filtersFragment)
        }
    }

    private fun getProducts() {
        var productName: String? = if (args.productName.isNullOrEmpty()) null else args.productName
        var categoryId: Int? = if (args.categoryId == -1) null else args.categoryId
        val onSale = args.onSale
        val supermarketIds = mutableSetOf<Int>()
        if (args.mercadonaId != -1) supermarketIds.add(args.mercadonaId)
        if (args.diaId != -1) supermarketIds.add(args.diaId)
        if (args.consumId != -1) supermarketIds.add(args.consumId)
        if (args.alcampoId != -1) supermarketIds.add(args.alcampoId)

        vm.setFilters(0, productName, categoryId, supermarketIds, onSale)

        if (checkConnection(requireContext())) {
            lifecycleScope.launch {
                vm.currentProducts.collect {
                    if (!it.empty) {
                        adapter.submitList(it.content)
                    } else {
                        adapter.submitList(emptyList())
                    }
                }
            }
        } else {
            adapter.submitList(emptyList())
            Toast.makeText(
                requireContext(),
                "No connection",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun initRecyclerView() {
        adapter = ProductListAdapter(
            assignToList = { product, position ->
                showAlertDialog()
            })
        binding.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerProducts.adapter = adapter
    }

    private fun showAlertDialog() {
        val bindingDialog = DialogAddProductBinding.inflate(layoutInflater)

        AlertDialog.Builder(requireContext()).apply {
            setView(bindingDialog.root)

            setPositiveButton(android.R.string.ok) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    "",
                    Toast.LENGTH_SHORT
                ).show()
            }

            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                Toast.makeText(context, android.R.string.cancel, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }.show()
    }
}
