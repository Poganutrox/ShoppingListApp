package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.ProductItemBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.Supermarkets.*

class ProductListAdapter(
    private val onClickFav: (productId: String, position : Int) -> Unit,
    private val onLongClickDetailView: (productId: String) -> Unit,
    private val onClickAskQuantity: (productId: String, position: Int) -> Unit
) : PagingDataAdapter<Product, ProductListAdapter.ProductListViewHolder>(ProductsDiffCallback()) {
    private val userFavourites = userPrefs.getLoggedUser().favouriteProductsId
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
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ProductListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ProductItemBinding = ProductItemBinding.bind(view)
        fun bind(product: Product) {
            with(binding) {
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    imgFav.setOnClickListener {
                        onClickFav(product.id, position)
                    }
                }

                productCard.setOnLongClickListener { onLongClickDetailView(product.id); true }
                productCard.setOnClickListener { onClickAskQuantity(product.id, layoutPosition) }

                tvName.text = product.name
                tvPrice.text = "${product.priceHistories?.let { it.get(0).price }} €"


                val quantity = addPrefs.getQuantityByProductId(product.id)
                if (quantity > 0) {
                    tvQuantity.text = quantity.toString()
                    tvQuantity.visibility = VISIBLE
                } else {
                    tvQuantity.visibility = INVISIBLE
                }

                when (product.supermarketId) {
                    MERCADONA.id -> imgLogo.setImageResource(MERCADONA.image)
                    DIA.id -> imgLogo.setImageResource(DIA.image)
                    ALCAMPO.id -> imgLogo.setImageResource(ALCAMPO.image)
                    CONSUM.id -> imgLogo.setImageResource(CONSUM.image)
                }

                val isFavourite = userFavourites?.contains(product.id) == true
                imgFav.setImageState(intArrayOf(R.attr.state_fav), isFavourite)


                try {
                    Glide.with(view)
                        .load(product.image)
                        .error(
                            AppCompatResources.getDrawable(
                                view.context,
                                R.drawable.no_image_available
                            )
                        )
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