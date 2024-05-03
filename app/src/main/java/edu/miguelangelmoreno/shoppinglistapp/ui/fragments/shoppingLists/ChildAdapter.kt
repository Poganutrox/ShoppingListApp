package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.miguelangelmoreno.shoppinglistapp.databinding.ProductNoteBinding
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListProduct

class ChildAdapter(private val onClickDelete: (productId: String, position : Int) -> Unit) :
    ListAdapter<ShoppingListProduct, ChildAdapter.ChildViewHolder>(
        ChildDiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChildAdapter.ChildViewHolder {
        return ChildViewHolder(
            ProductNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ChildAdapter.ChildViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ProductNoteBinding = ProductNoteBinding.bind(view)
        fun bind(shoppingListProduct: ShoppingListProduct) {
            with(binding) {
                tvProductName.text = shoppingListProduct.product.name
                tvQuantity.text = shoppingListProduct.quantity.toString()
                tvProductPrice.text =
                    shoppingListProduct.product.priceHistories.first().price.toString()
                btnDelete.setOnClickListener { onClickDelete(shoppingListProduct.product.id, layoutPosition)}
            }
        }
    }
}

class ChildDiffCallback : DiffUtil.ItemCallback<ShoppingListProduct>() {
    override fun areItemsTheSame(
        oldItem: ShoppingListProduct,
        newItem: ShoppingListProduct
    ): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(
        oldItem: ShoppingListProduct,
        newItem: ShoppingListProduct
    ): Boolean {
        return oldItem == newItem
    }
}