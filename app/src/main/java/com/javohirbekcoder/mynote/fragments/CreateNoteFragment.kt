package com.javohirbekcoder.mynote.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.data.AppData
import com.javohirbekcoder.mynote.database.DatabaseHelper
import com.javohirbekcoder.mynote.databinding.FragmentCreateNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedColorIndex = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(inflater)

        databaseHelper = DatabaseHelper(requireContext())

        binding.saveBtn.setOnClickListener {
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault())
            val formattedDateTime = dateFormat.format(currentDateTime)

            if (checkData()) {
                databaseHelper.insertNote(
                    binding.titeText.text.toString().trim(),
                    binding.noteText.text.toString(),
                    formattedDateTime,
                    selectedColorIndex
                )
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
            }else{
                Toast.makeText(requireContext(), "Please fill in the fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectColorBtn.setOnClickListener {
            val select_color_dialog = Dialog(requireContext())
            select_color_dialog.setContentView(R.layout.color_picker_dialog)
            select_color_dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


            val greenBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_green)
            val pinkBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_pink)
            val yellowBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_yellow)
            val blueBtn = select_color_dialog.findViewById<ImageView>(R.id.color_picker_blue)

            greenBtn.setOnClickListener {
                setTheme(0)
                selectedColorIndex = 0
                select_color_dialog.dismiss()
            }
            pinkBtn.setOnClickListener {
                setTheme(1)
                selectedColorIndex = 1
                select_color_dialog.dismiss()
            }
            yellowBtn.setOnClickListener {
                setTheme(2)
                selectedColorIndex = 2
                select_color_dialog.dismiss()
            }
            blueBtn.setOnClickListener {
                setTheme(3)
                selectedColorIndex = 3
                select_color_dialog.dismiss()
            }

            select_color_dialog.show()
        }
       /* binding.deleteBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Delete?", Toast.LENGTH_SHORT).show()
        }*/
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

    private fun checkData(): Boolean {
        return !(binding.noteText.text.isNullOrEmpty() || binding.titeText.text.isNullOrEmpty())
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

    private fun setTheme(index: Int) {

        val statusColor = AppData.StatusBarColors[index]
        val actionColor = AppData.ActionBarColors[index]
        //Status Bar
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(statusColor)

        //Action Bar
        binding.linear.setBackgroundColor(Color.parseColor(actionColor))
    }

}