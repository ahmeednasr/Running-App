package com.example.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.runningapp.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.runningapp.Constants.KEY_NAME
import com.example.runningapp.Constants.KEY_WEIGHT
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentSetupBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment() {
    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isFirstAppOpen) {
            val navOption = NavOptions.Builder().setPopUpTo(R.id.setupFragment, true).build()
            findNavController().navigate(
                SetupFragmentDirections.actionSetupFragmentToRunFragment(),
                navOption
            )
        }
        binding.btnContinue.setOnClickListener {
            val success = writePersonalDataTOSharedPref()
            if (success) {
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToRunFragment())

            } else {
                Snackbar.make(
                    binding.root,
                    "Please enter all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun writePersonalDataTOSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        val toolbarText = "Let's go, $name!"
        (requireActivity() as AppCompatActivity).findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText

        return true

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}