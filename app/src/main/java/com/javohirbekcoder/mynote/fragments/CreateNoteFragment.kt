package com.javohirbekcoder.mynote.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.databinding.FragmentCreateNoteBinding

class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(inflater)


        binding.saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }
        binding.selectColorBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }
        binding.deleteBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }
        binding.titeText.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty())
                binding.titleTextMain.text = "Your title"
            else
                binding.titleTextMain.text = text
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
        }
    }
}