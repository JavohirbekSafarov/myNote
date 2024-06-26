package com.javohirbekcoder.mynote.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.adapters.MainNotesAdapter
import com.javohirbekcoder.mynote.database.DatabaseHelper
import com.javohirbekcoder.mynote.databinding.ConfirmDeleteDialogBinding
import com.javohirbekcoder.mynote.databinding.FragmentHomeBinding
import com.javohirbekcoder.mynote.models.MainRecyclerModel

class HomeFragment : Fragment(R.layout.fragment_home), MainNotesAdapter.myItemOnClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainNotesAdapter: MainNotesAdapter

    private lateinit var databaseHelper: DatabaseHelper

    private var searching = false

    private val sortingNames = arrayListOf(
        "date modified",
        "olds first",
        "color",
        "title"
    )
    private var mainRecyclerItem = ArrayList<MainRecyclerModel>()
    private var searchingList = ArrayList<MainRecyclerModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        setStatusBarColor()
        setSpinner()
        setRecyclerAdapter()

        binding.searchImg.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (!searching) {
                    binding.searchImg.setImageResource(R.drawable.close)
                    binding.searchText.isEnabled = true
                    searching = true
                } else {
                    binding.searchImg.setImageResource(R.drawable.search)
                    binding.searchText.isEnabled = false
                    binding.searchText.text.clear()
                    searching = false
                }
            }
            false
        }
        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment)
        }
        binding.searchText.doOnTextChanged { text, start, before, count ->
            search(text)
        }

        // set empty list animation

       /* when (requireContext().resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Glide.with(requireContext()).load(R.drawable.empty_list_dark).into(binding.emptyListIcon)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Glide.with(requireContext()).load(R.drawable.empty_list_white).into(binding.emptyListIcon)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                Glide.with(requireContext()).load(R.drawable.empty_list).into(binding.emptyListIcon)
            }
        }*/

        return binding.root
    }

    private fun search(text: CharSequence?) {
        searchingList.clear()
        for (list in mainRecyclerItem) {
            if (list.title.contains(text.toString(), true) || list.note.contains(
                    text.toString(),
                    true
                )
            ) {
                searchingList.add(list)
            }
        }

        mainNotesAdapter = MainNotesAdapter(requireContext(), searchingList, this)
        binding.mainRecyclerview.adapter = mainNotesAdapter
        binding.allNotesCount.text = mainNotesAdapter.itemCount.toString()
    }

    private fun setRecyclerAdapter() {
        databaseHelper = DatabaseHelper(requireContext())

        readDataFromDatabase()

        setEmptyLayout()

        mainNotesAdapter = MainNotesAdapter(requireContext(), mainRecyclerItem, this)
        binding.mainRecyclerview.adapter = mainNotesAdapter
    }

    private fun readDataFromDatabase() {
        mainRecyclerItem.clear()
        val cursor = databaseHelper.readNote()
        if (cursor.moveToFirst()) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(0)
                    val title = cursor.getString(1)
                    val note = cursor.getString(2)
                    val date = cursor.getString(3)
                    val colorIndex = cursor.getInt(4)

                    val item = MainRecyclerModel(id, colorIndex, title, note, date)
                    mainRecyclerItem.add(item)
                }
            }
        } else
            mainRecyclerItem.clear()
    }

    private fun setStatusBarColor() {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#5678F1")
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.sort_spinner_item,
            R.id.spinner_tv,
            sortingNames
        )
        adapter.setDropDownViewResource(R.layout.sort_spinner_item)

        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (mainRecyclerItem.isNotEmpty()) {
                    when (position) {
                        0 -> {
                            mainRecyclerItem.sortBy { it.time }
                            mainRecyclerItem.reverse()
                        }
                        1 -> {
                            mainRecyclerItem.sortBy { it.time }
                        }
                        2 -> {
                            mainRecyclerItem.sortBy { it.colorIndex }
                        }
                        3 -> {
                            mainRecyclerItem.sortBy { it.title }
                        }
                        else -> {
                            mainRecyclerItem.sortBy { it.time }
                        }
                    }
                    mainNotesAdapter.notifyItemRangeChanged(0, mainRecyclerItem.size)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun mItemClickListener(position: Int) {
        val bundle = Bundle()
        bundle.putInt("id", mainRecyclerItem[position].id)
        bundle.putString("title", mainRecyclerItem[position].title)
        bundle.putString("note", mainRecyclerItem[position].note)
        bundle.putString("time", mainRecyclerItem[position].time)
        bundle.putInt("colorIndex", mainRecyclerItem[position].colorIndex)
        findNavController().navigate(R.id.action_homeFragment_to_openNoteFragment, bundle)
    }

    override fun onLongClickListener(position: Int) {

        val confirmDeleteDialog = Dialog(requireContext())
        val dialogBinding = ConfirmDeleteDialogBinding.inflate(layoutInflater)
        confirmDeleteDialog.setContentView(dialogBinding.root)

        dialogBinding.questionTextView.text =
            "Are you sure want to delete ${mainRecyclerItem[position].title} ?"
        dialogBinding.cancelButton.setOnClickListener {
            confirmDeleteDialog.dismiss()
        }
        dialogBinding.yesButton.setOnClickListener {
            databaseHelper.deleteNote(mainRecyclerItem[position].title)
            mainRecyclerItem.removeAt(position)
            mainNotesAdapter.notifyItemRemoved(position)
            binding.allNotesCount.text = mainNotesAdapter.itemCount.toString()
            setEmptyLayout()
            confirmDeleteDialog.dismiss()
        }
        confirmDeleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        confirmDeleteDialog.show()
    }

    private fun setEmptyLayout() {
        if (mainRecyclerItem.isEmpty()) {
            binding.mainRecyclerview.visibility = View.INVISIBLE
            binding.emptyListLayout.visibility = View.VISIBLE

            binding.allNotesCount.text = "0"
        } else {
            binding.mainRecyclerview.visibility = View.VISIBLE
            binding.emptyListLayout.visibility = View.INVISIBLE

            binding.allNotesCount.text = mainRecyclerItem.size.toString()
        }
    }

}