package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.ProductItemBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.Supermarkets.*

class ProductListAdapter(
    private val onClickFav: (productId: String, btnFav : ImageButton) -> Unit,
    private val onClickDetailView : (productId : String) -> Unit
) : PagingDataAdapter<Product, ProductListAdapter.ProductListViewHolder>(ProductsDiffCallback()) {
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
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ProductListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ProductItemBinding = ProductItemBinding.bind(view)
        fun bind(product: Product) {
            with(binding) {
                imgFav.setOnClickListener { onClickFav(product.id, imgFav) }
                productCard.setOnClickListener {  onClickDetailView(product.id) }

                tvName.text = product.name
                tvPrice.text = "${product.priceHistories?.let { it.get(0).price }} €"

                when (product.supermarketId) {
                    MERCADONA.id -> imgLogo.setImageResource(MERCADONA.image)
                    DIA.id -> imgLogo.setImageResource(DIA.image)
                    ALCAMPO.id -> imgLogo.setImageResource(ALCAMPO.image)
                    CONSUM.id -> imgLogo.setImageResource(CONSUM.image)
                    else -> null
                }

                if (userPrefs.getLoggedUser().favouriteProductsId?.contains(product.id) == true) {
                    imgFav.setImageState(intArrayOf(R.attr.state_fav), true)
                }else{
                    imgFav.setImageState(intArrayOf(R.attr.state_fav), false)
                }

                try {
                    Glide.with(view)
                        .load(product.image)
                        .error(view.context.getDrawable(R.drawable.no_image_available))
                        .into(imgProduct)
                } catch (e: Exception) {
                    Log.i("Excepción en Glide", e.toString())
                }
            }

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