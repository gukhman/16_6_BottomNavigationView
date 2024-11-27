package com.example.a16_6_bottomnavigationview.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a16_6_bottomnavigationview.Note

class NotesViewModel : ViewModel() {

    private val _notes = MutableLiveData<MutableList<Note>>().apply {
        value = mutableListOf()
    }
    val notes: LiveData<MutableList<Note>> = _notes

    fun addNote(description: String) {
        val newNote = Note(id = _notes.value?.size ?: 0, description = description, isCompleted = false)
        _notes.value?.add(newNote)
        _notes.value = _notes.value
    }

    fun deleteTask(note: Note) {
        _notes.value?.remove(note)
        _notes.value = _notes.value
    }

    fun updateNote(note: Note) {
        val noteIndex = _notes.value?.indexOfFirst { it.id == note.id }
        if (noteIndex != null && noteIndex != -1) {
            _notes.value?.set(noteIndex, note)
            _notes.value = _notes.value
        }
    }
}
