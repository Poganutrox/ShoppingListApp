package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.productdetails

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentProductDetailsBinding
import edu.miguelangelmoreno.shoppinglistapp.model.PriceHistory
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private val vm: ProductDetailsViewModel by viewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getClickedProduct()
        observeProductChanges()
    }

    private fun observeProductChanges() {
        lifecycleScope.launch {
            combine(vm.product, vm.priceHistory) { product, priceHistory ->
                Pair(product, priceHistory)
            }.collect { (product, priceHistory) ->
                loadLineChart(product, priceHistory)
            }
        }
    }


    private fun loadLineChart(product:Product, priceHistory: List<PriceHistory>) {
        val dates = priceHistory.map { it.date }

        with(binding.lcPrices) {
            description.isEnabled = false
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(dates)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.labelRotationAngle = -45f
            xAxis.granularity = 1f

            val entries: MutableList<Entry> = mutableListOf()
            priceHistory.forEachIndexed { index, history ->
                entries.add(Entry(index.toFloat(), history.price.toFloat()))
            }

            val dataSet = LineDataSet(entries, "Precio €")
            dataSet.setDrawCircles(true)
            dataSet.setCircleColor(ContextCompat.getColor(context, R.color.md_theme_light_primary))
            dataSet.circleRadius = 3f
            dataSet.setDrawValues(false)
            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            dataSet.setDrawFilled(true)
            dataSet.fillColor = ContextCompat.getColor(context, R.color.md_theme_light_primary)
            dataSet.fillAlpha = 50

            val lineData = LineData(dataSet)
            data = lineData
            Log.i("Precio máximo",data.yMax.toString())
            Log.i("Precio mínimo",data.yMin.toString())
            invalidate()
        }
    }





    private fun getClickedProduct() {
        vm.getClickedProduct(args.productId)
    }
}