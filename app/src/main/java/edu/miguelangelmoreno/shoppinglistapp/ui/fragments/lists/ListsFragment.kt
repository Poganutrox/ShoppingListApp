package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.lists

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.DialogAddProductBinding
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentListsBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Supermarket
import edu.miguelangelmoreno.shoppinglistapp.utils.checkConnection
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListsFragment : Fragment() {
    private lateinit var binding: FragmentListsBinding
    private val vm: ListsViewModel by viewModels()
    private lateinit var adapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSupermarketDropDown()
        initRecyclerView()
        getProducts()
    }

    private fun getProducts() {
        adapter.submitList(emptyList())
        if (checkConnection(requireContext())) {
            lifecycleScope.launch {
                vm.currentProducts.collect {
                    adapter.submitList(it)
                }
            }
        } else {
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
        binding.recyclerProducts.adapter = adapter
    }

    private fun initSupermarketDropDown() {
        val supermarkets = listOf(
            Supermarket("Mercadona", R.mipmap.ic_mercadona),
            Supermarket("Carrefour", R.mipmap.ic_carrefour),
        )

        val adapter = SupermarketAdapter(requireContext(), supermarkets)
        val autoCompleteTextView = binding.actvComboSupermarket
        autoCompleteTextView.setAdapter(adapter)
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
