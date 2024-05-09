package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.filters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
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
    ): View {
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
            vm.currentCategories.collect { result ->
                if (result.isSuccessful) {
                    val categories = result.body()
                    if (categories != null) {
                        val spinnerAdapter = object : ArrayAdapter<Category>(
                            binding.root.context,
                            android.R.layout.simple_spinner_item,
                            categories
                        ) {
                            override fun getView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val view = super.getView(position, convertView, parent)
                                val category = getItem(position)
                                view.findViewById<TextView>(android.R.id.text1).text =
                                    category?.name
                                return view
                            }

                            override fun getDropDownView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val view = super.getDropDownView(position, convertView, parent)
                                val category = getItem(position)
                                view.findViewById<TextView>(android.R.id.text1).text =
                                    category?.name
                                return view
                            }
                        }
                        binding.spinnerCategories.adapter = spinnerAdapter.also {
                            it.insert(Category(-1, ""), 0)
                            binding.spinnerCategories.setSelection(0)
                        }
                    }
                }
            }
        }
    }


    private fun initListeners() {
        var categoryId: Int = -1
        var mercadonaId = -1
        var diaId = -1
        var consumId = -1
        var alcampoId = -1
        var priceSort = -1
        var alphabeticSort = -1
        var onSale = false

        with(binding) {
            chipMercadona.setOnCheckedChangeListener { _, isChecked ->
                mercadonaId = when (isChecked) {
                    true -> 1
                    else -> -1
                }
            }
            chipDia.setOnCheckedChangeListener { _, isChecked ->
                diaId = when (isChecked) {
                    true -> 2
                    else -> -1
                }
            }
            chipConsum.setOnCheckedChangeListener { _, isChecked ->
                consumId = when (isChecked) {
                    true -> 4
                    else -> -1
                }
            }
            chipAlcampo.setOnCheckedChangeListener { _, isChecked ->
                alcampoId = when (isChecked) {
                    true -> 3
                    else -> -1
                }
            }
            chipGroupPrices.setOnCheckedStateChangeListener { _, checkedIdList ->
                priceSort = if (checkedIdList.isNotEmpty()) {
                    when (checkedIdList[0]) {
                        chipPriceAsc.id -> 1
                        chipPriceDesc.id -> 2
                        else -> -1
                    }
                } else {
                    -1
                }
            }
            chipGroupAlphabetic.setOnCheckedStateChangeListener { _, checkedIdList ->
                alphabeticSort = if (checkedIdList.isNotEmpty()) {
                    when (checkedIdList[0]) {
                        chipAlphabeticAZ.id -> 1
                        chipAlphabeticZA.id -> 2
                        else -> -1
                    }
                } else {
                    -1
                }
            }
            imgOnSale.setOnClickListener {
                onSale = !onSale
                imgOnSale.setImageState(
                    intArrayOf(R.drawable.sale),
                    when (onSale) {
                        true -> true
                        false -> false
                    }
                )
            }
            spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position)
                    selectedItem?.let {
                        categoryId = (selectedItem as Category).id
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            btnFilters.setOnClickListener {
                val productName: String = etProductName.text.toString()

                findNavController().navigate(
                    FiltersFragmentDirections.actionFiltersFragmentToProductsFragment(
                        categoryId = categoryId,
                        productName = productName,
                        mercadonaId = mercadonaId,
                        alcampoId = alcampoId,
                        diaId = diaId,
                        consumId = consumId,
                        alphabeticSort = alphabeticSort,
                        priceSort = priceSort,
                        onSale = onSale
                    )
                )
            }
        }


    }
}