package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentShoppingListsBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingListsFragment : Fragment() {
    private lateinit var binding: FragmentShoppingListsBinding
    private lateinit var mainAdapter: MainAdapter
    private val vm: ShoppingListsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecycler()
        observeViewModelChanges()
    }

    private fun initListeners() {
        binding.fabAddList.setOnClickListener { view ->
            showPopUpMenu(view)
        }
    }

    private fun observeViewModelChanges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.shoppingListState.collect { state ->
                    binding.progressBar.isVisible = state.isShoppingListLoading
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.shoppingLists.collect {
                    if (it.isNotEmpty()) {
                        mainAdapter.submitList(it)
                    }

                }
            }
        }
    }

    private fun initRecycler() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val shoppingList = mainAdapter.currentList[position]

                if (direction == ItemTouchHelper.LEFT) {
                    vm.deleteShoppingList(shoppingList, userPrefs.getUserId()!!)
                    Snackbar.make(
                        binding.root,
                        "${shoppingList.name} eliminado.",
                        Snackbar.LENGTH_LONG
                    ).setAction("Deshacer") {
                        vm.removeChanges(shoppingList, userPrefs.getUserId()!!)
                    }.show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    addPrefs.setAddingMode(shoppingList)
                    findNavController().navigate(
                        ShoppingListsFragmentDirections.actionHomeFragmentToAddProductFragment(
                            id = shoppingList.id!!,
                            name = shoppingList.name!!
                        )
                    )
                }
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                if (dX > 0) {
                    val iconEdit: Drawable? =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_edit_alt)
                    iconEdit?.let { iconEdit ->
                        val leftMargin = (viewHolder.itemView.height - iconEdit.intrinsicHeight) / 2
                        val iconTopLeftCorner: Int =
                            viewHolder.itemView.top + (viewHolder.itemView.height - iconEdit.intrinsicHeight) / 2
                        val iconBottomLeftCorner: Int = iconTopLeftCorner + iconEdit.intrinsicHeight
                        val iconLeftMargin: Int = viewHolder.itemView.left + leftMargin
                        val iconRightMargin: Int =
                            viewHolder.itemView.left + leftMargin + iconEdit.intrinsicWidth
                        iconEdit.setBounds(
                            iconLeftMargin,
                            iconTopLeftCorner,
                            iconRightMargin,
                            iconBottomLeftCorner
                        )
                        val background = ColorDrawable(Color.BLUE)
                        background.setBounds(
                            viewHolder.itemView.left,
                            viewHolder.itemView.top,
                            (viewHolder.itemView.left + dX.toInt()).coerceAtMost(viewHolder.itemView.right),
                            viewHolder.itemView.bottom
                        )
                        background.draw(c)
                        iconEdit.draw(c)
                    }
                } else if (dX < 0) {
                    val iconTrash: Drawable? =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_trash_alt)
                    iconTrash?.let { iconTrash ->
                        val leftMargin =
                            (viewHolder.itemView.height - iconTrash.intrinsicHeight) / 2
                        val iconTopLeftCorner: Int =
                            viewHolder.itemView.top + (viewHolder.itemView.height - iconTrash.intrinsicHeight) / 2
                        val iconBottomLeftCorner: Int =
                            iconTopLeftCorner + iconTrash.intrinsicHeight
                        val iconLeftMargin: Int =
                            viewHolder.itemView.right - leftMargin - iconTrash.intrinsicWidth
                        val iconRightMargin: Int = viewHolder.itemView.right - leftMargin
                        iconTrash.setBounds(
                            iconLeftMargin,
                            iconTopLeftCorner,
                            iconRightMargin,
                            iconBottomLeftCorner
                        )
                        val background = ColorDrawable(Color.RED)
                        background.setBounds(
                            (viewHolder.itemView.right + dX.toInt()).coerceAtLeast(
                                viewHolder.itemView.left
                            ),
                            viewHolder.itemView.top,
                            viewHolder.itemView.right,
                            viewHolder.itemView.bottom
                        )
                        background.draw(c)
                        iconTrash.draw(c)
                    }
                }
            }


        }).attachToRecyclerView(binding.mainRecyclerView)

        mainAdapter = MainAdapter()
        binding.mainRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.mainRecyclerView.adapter = mainAdapter
    }

    private fun showPopUpMenu(view: View?) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.shopping_list_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opt_new_list -> {
                    findNavController().navigate(R.id.action_homeFragment_to_addProductFragment)
                    true
                }

                R.id.opt_import_list -> {
                    findNavController().navigate(R.id.action_homeFragment_to_importCodeFragment)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}