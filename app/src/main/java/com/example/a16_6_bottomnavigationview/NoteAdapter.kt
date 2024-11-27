package com.example.a16_6_bottomnavigationview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val onTaskAction: (Note, NoteAction) -> Unit) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var noteList = mutableListOf<Note>()

    fun submitList(notes: MutableList<Note>) {
        noteList = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = noteList.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskDescription: TextView = itemView.findViewById(R.id.noteText)
        private val taskCheckbox: CheckBox = itemView.findViewById(R.id.noteCheckbox)
        private val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(note: Note) {
            taskDescription.text = note.description
            taskCheckbox.isChecked = note.isCompleted

            taskCheckbox.setOnCheckedChangeListener { _, _ ->
                onTaskAction(note, NoteAction.TOGGLE_COMPLETE)
            }
            editButton.setOnClickListener {
                onTaskAction(note, NoteAction.EDIT)
            }
            deleteButton.setOnClickListener {
                onTaskAction(note, NoteAction.DELETE)
            }
        }
    }
}

enum class NoteAction {
    EDIT,
    DELETE,
    TOGGLE_COMPLETE
}
