package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.filters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentFiltersBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FiltersFragment : Fragment() {
    private lateinit var binding: FragmentFiltersBinding
    private val vm: FiltersViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        lifecycleScope.launch {
            vm.currentCategories.collect{ result ->
                val categories = result.result ?: listOf()

                val spinnerAdapter = object : ArrayAdapter<Category>(
                    binding.root.context,
                    com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
                    categories
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent)
                        val category = getItem(position)
                        view.findViewById<TextView>(android.R.id.text1).text = category?.name
                        return view
                    }

                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getDropDownView(position, convertView, parent)
                        val category = getItem(position)
                        view.findViewById<TextView>(android.R.id.text1).text = category?.name
                        return view
                    }
                }

                binding.spinnerCategories.adapter = spinnerAdapter
            }
        }
    }

    private fun initListeners() {
        var categoryId: Int = -1
        var mercadonaId = -1
        var diaId = -1
        var consumId = -1
        var alcampoId = -1

        binding.chipMercadona.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> mercadonaId = 1
                else -> mercadonaId = -1
            }
        }
        binding.chipDia.setOnCheckedChangeListener { _, isChecked ->

            when(isChecked){
                true -> diaId = 2
                else -> diaId = -1
            }
        }
        binding.chipConsum.setOnCheckedChangeListener { _, isChecked ->

            when(isChecked){
                true -> consumId = 4
                else -> consumId = -1
            }
        }
        binding.chipAlcampo.setOnCheckedChangeListener { _, isChecked ->

            when(isChecked){
                true -> mercadonaId = 3
                else -> mercadonaId = -1
            }
        }
        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                selectedItem?.let {
                    categoryId = (selectedItem as Category).id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnFilters.setOnClickListener {
            val productName: String? = binding.etProductName.text.toString()

            findNavController().navigate(FiltersFragmentDirections.actionFiltersFragmentToProductsFragment(
                categoryId = categoryId,
                productName = productName,
                mercadonaId = mercadonaId,
                alcampoId = alcampoId,
                diaId = diaId,
                consumId = consumId
            ))
        }
    }
}