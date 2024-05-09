package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.NoteItemBinding
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList

class MainAdapter :
    ListAdapter<ShoppingList, MainAdapter.MainViewHolder>(
        MainDiffCallback()
    ) {
    private val userId = userPrefs.getUserId()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        return MainViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MainViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding: NoteItemBinding = NoteItemBinding.bind(view)
        fun bind(shoppingList: ShoppingList) {
            val childAdapter = ChildAdapter(
                onClickDelete = { _, _ -> }
            )
            childAdapter.submitList(shoppingList.shoppingListProducts)
            with(binding) {
                val randomColor = getRandomColor()
                cardShoppingList.setCardBackgroundColor(view.context.resources.getColor(randomColor))
                tvListTitle.text = shoppingList.name
                tvCreationDate.text =
                    view.context.getString(
                        R.string.shopping_list_created_at,
                        shoppingList.creationDate
                    )
                val finalCost = shoppingList.shoppingListProducts?.sumOf { shoppingListProduct ->
                    shoppingListProduct.product.priceHistories.first().price * shoppingListProduct.quantity
                }
                tvFinalPrice.text = view.context.getString(R.string.total_price, finalCost)

                if (shoppingList.creatorUser?.id == userId) {
                    if (shoppingList.uniqueShareCode != null) {
                        tvUniqueShareCode.text = view.context.getString(
                            R.string.friend_share_code,
                            shoppingList.uniqueShareCode
                        )
                    }
                }

                productList.layoutManager =
                    LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                productList.adapter = childAdapter
            }
        }
    }

    private fun getRandomColor(): Int {
        val shoppingListColors: List<Int> = listOf(
            R.color.cardColor_1,
            R.color.cardColor_2,
            R.color.cardColor_3,
            R.color.cardColor_4,
            R.color.cardColor_5,
            R.color.cardColor_6
        )
        val randomIndex = (shoppingListColors.indices).random()
        return shoppingListColors[randomIndex]
    }
}

class MainDiffCallback : DiffUtil.ItemCallback<ShoppingList>() {
    override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem == newItem
    }
}