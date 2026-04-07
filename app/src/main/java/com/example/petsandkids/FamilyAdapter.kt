package com.example.petsandkids

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * Адаптер для списка членов семьи
 * Создаёт карточки для каждого ребёнка или питомца
 */
class FamilyAdapter(
    private val onItemClick: (FamilyMember) -> Unit
) : ListAdapter<FamilyMember, FamilyAdapter.FamilyViewHolder>(FamilyDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_family, parent, false)
        return FamilyViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: FamilyViewHolder, position: Int) {
        val member = getItem(position)
        holder.bind(member, onItemClick)
    }
    
    class FamilyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val tvBirthDate: TextView = itemView.findViewById(R.id.tvBirthDate)
        
        fun bind(member: FamilyMember, onItemClick: (FamilyMember) -> Unit) {
            tvName.text = member.name
            tvBirthDate.text = "Дата рождения: ${member.birthDate}"
            
            // Устанавливаем иконку и текст в зависимости от типа
            if (member.type == "child") {
                ivIcon.setImageResource(android.R.drawable.presence_online)  // 👶 эмодзи не в ресурсах, используем иконку
                tvType.text = "👶 Ребёнок"
            } else {
                val petTypeStr = member.petType ?: "питомец"
                tvType.text = "🐾 $petTypeStr${if (member.breed != null) " (${member.breed})" else ""}"
                ivIcon.setImageResource(android.R.drawable.presence_away)  // 🐾
            }
            
            itemView.setOnClickListener { onItemClick(member) }
        }
    }
    
    class FamilyDiffCallback : DiffUtil.ItemCallback<FamilyMember>() {
        override fun areItemsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean {
            return oldItem == newItem
        }
    }
}
