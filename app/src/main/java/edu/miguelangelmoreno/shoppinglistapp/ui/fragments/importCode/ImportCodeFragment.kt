package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.importCode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentImportCodeBinding
import edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists.ShoppingListsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImportCodeFragment : Fragment() {
    private lateinit var binding: FragmentImportCodeBinding
    private val vm: ShoppingListsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImportCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeStateChanges()

    }
    private fun observeStateChanges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.shoppingListState.collect {state ->
                    binding.progressBar.isVisible = state.isImportLoading
                    if(state.isImportReceived){
                        if(state.isImportSuccess){
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun initListeners(){
        binding.btnImport.setOnClickListener {
            val uniqueCode = binding.etCode.text.toString()
            vm.importList(uniqueCode)
        }
    }
}