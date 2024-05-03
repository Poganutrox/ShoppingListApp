package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.favourites

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication
import edu.miguelangelmoreno.shoppinglistapp.databinding.ProductItemFavBinding
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.Supermarkets

class FavouritesAdapter(
    private val onClickFav: (productId: String) -> Unit,
    private val onLongClickDetailView : (productId : String) -> Unit
) : ListAdapter<Product, FavouritesAdapter.FavouritesViewHolder>(FavouritesDiffCallback()){
    private val userFavourites = ShoppingListApplication.userPrefs.getLoggedUser().favouriteProductsId
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(
            ProductItemFavBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavouritesViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        private val binding : ProductItemFavBinding = ProductItemFavBinding.bind(view)
        fun bind(product: Product){
            with(binding) {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    imgFav.setOnClickListener {
                        onClickFav(product.id)
                    }
                }

                productCard.setOnLongClickListener {  onLongClickDetailView(product.id); true}

                tvName.text = product.name
                tvPrice.text = "${product.priceHistories?.let { it.get(0).price }} €"

                when (product.supermarketId) {
                    Supermarkets.MERCADONA.id -> imgLogo.setImageResource(Supermarkets.MERCADONA.image)
                    Supermarkets.DIA.id -> imgLogo.setImageResource(Supermarkets.DIA.image)
                    Supermarkets.ALCAMPO.id -> imgLogo.setImageResource(Supermarkets.ALCAMPO.image)
                    Supermarkets.CONSUM.id -> imgLogo.setImageResource(Supermarkets.CONSUM.image)
                    else -> null
                }

                val isFavourite = userFavourites?.contains(product.id) == true
                imgFav.setImageState(intArrayOf(R.attr.state_fav), isFavourite)


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

class FavouritesDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}