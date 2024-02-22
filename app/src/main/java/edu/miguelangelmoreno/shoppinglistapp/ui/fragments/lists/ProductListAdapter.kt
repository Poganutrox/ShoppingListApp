package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.ProductItemBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Product

class ProductListAdapter(
    private val assignToList : (Product, (Int)) -> Unit
) : ListAdapter<Product, ProductListAdapter.ProductListViewHolder>(ProductsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val binding : ProductItemBinding = ProductItemBinding.bind(view)
        fun bind(product: Product){
            binding.tvName.text = product.name
            binding.tvPrice.text = "${product.currentPrice} ${product.unit}"
            binding.tvSize.text = product.size

            if(product.supermarket == 1){
                binding.imgLogo.setImageResource(R.mipmap.ic_mercadona_foreground)
            }else{
                binding.imgLogo.setImageResource(R.mipmap.ic_carrefour_foreground)
            }

            Glide.with(view)
                .load(product.image)
                .into(binding.imgProduct)

            binding.imgAdd.setOnClickListener { assignToList(product, adapterPosition) }
        }
    }
}
class ProductsDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}