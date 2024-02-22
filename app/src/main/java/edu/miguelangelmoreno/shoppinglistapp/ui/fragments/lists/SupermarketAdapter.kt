package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import edu.miguelangelmoreno.shoppinglistapp.databinding.SupermarketDropdownItemBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Supermarket

class SupermarketAdapter(
    context: Context,
    supermarkets: List<Supermarket>
) : ArrayAdapter<Supermarket>(context, 0, supermarkets) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var binding = SupermarketDropdownItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val currentItem = getItem(position)
        currentItem?.let {
            binding.textView.text = it.name
            binding.imageView.setImageResource(it.icon)
        }

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
