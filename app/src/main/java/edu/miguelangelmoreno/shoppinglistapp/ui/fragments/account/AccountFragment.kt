package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.prefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.FragmentAccountBinding
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginActivity
import edu.miguelangelmoreno.shoppinglistapp.utils.makeToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val vm: AccountViewModel by viewModels()
    private lateinit var logUser: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLoggedUser()
        observeAccountState()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnEditUser.setOnClickListener {
                logUser.name = etName.text.toString()
                logUser.lastName = etLastName.text.toString()
                logUser.email = etEmail.text.toString()
                logUser.phone = etPhone.text.toString()
                vm.updateUser(logUser)

            }
            btnEndSession.setOnClickListener {
                prefs.clear()
                LoginActivity.navigate(requireContext())
                finishAffinity(requireActivity())
            }
        }
    }

    private fun observeAccountState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.accountState.collect { accountState ->
                    with(binding) {
                        progressBar.isVisible = accountState.isLoading
                    }

                    val errorMessage : String? = accountState.errorMessage?.let { getString(it) }
                    if (!errorMessage.isNullOrEmpty()) {
                        makeToast(requireContext(), errorMessage)
                    } else if (accountState.isUpdated) {
                        makeToast(requireContext(), getString(R.string.user_updated_success))
                        updatePrefs(logUser)
                    }
                }
            }
        }
    }

    private fun updatePrefs(user: User) {
        prefs.updateLoggedUser(user)
    }

    private fun loadLoggedUser() {
        logUser = prefs.getLoggedUser()
        with(binding) {
            etName.setText(logUser.name)
            etLastName.setText(logUser.lastName)
            etEmail.setText(logUser.email)
            etPhone.setText(logUser.phone)
        }

    }
}