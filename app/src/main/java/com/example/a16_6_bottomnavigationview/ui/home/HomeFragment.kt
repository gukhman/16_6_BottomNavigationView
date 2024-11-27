package com.example.a16_6_bottomnavigationview.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a16_6_bottomnavigationview.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Наблюдение за изменениями логина и пароля
        homeViewModel.login.observe(viewLifecycleOwner) {
            binding.loginTV.text = it
        }

        homeViewModel.password.observe(viewLifecycleOwner) {
            binding.passwordTV.text = it
        }

        binding.loginBTN.setOnClickListener {
            val login = binding.loginET.text.toString()
            val password = binding.passwordET.text.toString()
            homeViewModel.updateLogin(login)
            homeViewModel.updatePassword(password)
            binding.loginET.text.clear()
            binding.passwordET.text.clear()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
