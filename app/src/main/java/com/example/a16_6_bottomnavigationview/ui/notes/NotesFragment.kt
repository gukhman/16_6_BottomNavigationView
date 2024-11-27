package com.example.a16_6_bottomnavigationview.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a16_6_bottomnavigationview.Note
import com.example.a16_6_bottomnavigationview.NoteAction
import com.example.a16_6_bottomnavigationview.NoteAdapter
import com.example.a16_6_bottomnavigationview.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]

        _binding = FragmentNotesBinding.inflate(inflater, container, false)

        val adapter = NoteAdapter { note, action ->
            when (action) {
                NoteAction.EDIT -> showEditNoteDialog(note)
                NoteAction.DELETE -> notesViewModel.deleteTask(note)
                NoteAction.TOGGLE_COMPLETE -> {
                    note.isCompleted = !note.isCompleted
                    notesViewModel.updateNote(note)
                }
            }
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        notesViewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes.toMutableList())
        }

        binding.addNoteBTN.setOnClickListener {
            showAddNoteDialog()
        }

        return binding.root
    }

    private fun showAddNoteDialog() {
        val input = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("Добавить заметку")
            .setView(input)
            .setPositiveButton("Добавить") { _, _ ->
                val noteDescription = input.text.toString()
                if (noteDescription.isNotEmpty()) {
                    notesViewModel.addNote(noteDescription)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showEditNoteDialog(note: Note) {
        val input = EditText(requireContext()).apply { setText(note.description) }
        AlertDialog.Builder(requireContext())
            .setTitle("Редактировать заметку")
            .setView(input)
            .setPositiveButton("Применить") { _, _ ->
                val newDescription = input.text.toString()
                if (newDescription.isNotEmpty()) {
                    note.description = newDescription
                    notesViewModel.updateNote(note)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
