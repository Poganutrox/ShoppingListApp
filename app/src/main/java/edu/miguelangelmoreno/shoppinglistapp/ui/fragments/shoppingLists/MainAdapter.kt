package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.NoteItemBinding
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList

class MainAdapter ( private val onClickEdit : (shoppingList : ShoppingList) -> Unit) : ListAdapter<ShoppingList, MainAdapter.MainViewHolder>(
    MainDiffCallback()
) {
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
                onClickDelete = { _, _ ->}
            )
            childAdapter.submitList(shoppingList.shoppingListProducts)
            with(binding) {
                val randomColor = getRandomColor()
                cardShoppingList.setCardBackgroundColor(view.context.resources.getColor(randomColor))
                tvListTitle.text = shoppingList.name
                tvCreationDate.text = shoppingList.creationDate

                cardShoppingList.setOnClickListener { onClickEdit(shoppingList) }
                productList.setOnClickListener { onClickEdit(shoppingList) }

                productList.layoutManager =
                    LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                productList.adapter = childAdapter
            }
        }
    }

    private fun getRandomColor() : Int{
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