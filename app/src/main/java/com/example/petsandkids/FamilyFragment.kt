package com.example.petsandkids

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

/**
 * Фрагмент "Все члены семьи"
 * Здесь показывается список детей и питомцев
 */
class FamilyFragment : Fragment() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FamilyAdapter
    private lateinit var database: FamilyDatabase
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_family, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Получаем базу данных
        database = FamilyDatabase.getDatabase(requireContext())
        
        // Настраиваем список
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FamilyAdapter { member ->
            // Когда нажимаем на элемент - спрашиваем, удалить ли
            showDeleteDialog(member)
        }
        recyclerView.adapter = adapter
        
        // Загружаем данные из базы
        loadData()
        
        // Настраиваем кнопку добавления
        val fab: View = view.findViewById(R.id.fab_add)
        fab.setOnClickListener {
            showAddDialog()
        }
    }
    
    private fun loadData() {
        lifecycleScope.launch {
            database.familyMemberDao().getAll().collect { members ->
                adapter.submitList(members)
                updateStats(members.size)
            }
        }
    }
    
    private fun updateStats(count: Int) {
        val textView: TextView = requireView().findViewById(R.id.tvStats)
        textView.text = "Всего членов семьи: $count"
    }
    
    private fun showAddDialog() {
        // Создаём диалоговое окно для добавления
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_member, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etBirthDate = dialogView.findViewById<EditText>(R.id.etBirthDate)
        val rgType = dialogView.findViewById<RadioGroup>(R.id.rgType)
        val llPetFields = dialogView.findViewById<LinearLayout>(R.id.llPetFields)
        val etPetType = dialogView.findViewById<EditText>(R.id.etPetType)
        val etBreed = dialogView.findViewById<EditText>(R.id.etBreed)
        
        // Показываем/скрываем поля для питомца
        rgType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbPet) {
                llPetFields.visibility = View.VISIBLE
            } else {
                llPetFields.visibility = View.GONE
            }
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle("Добавить члена семьи")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = etName.text.toString()
                val birthDate = etBirthDate.text.toString()
                val isPet = rgType.checkedRadioButtonId == R.id.rbPet
                
                if (name.isNotBlank() && birthDate.isNotBlank()) {
                    val member = if (isPet) {
                        FamilyMember(
                            name = name,
                            type = "pet",
                            petType = etPetType.text.toString(),
                            breed = etBreed.text.toString(),
                            birthDate = birthDate
                        )
                    } else {
                        FamilyMember(
                            name = name,
                            type = "child",
                            petType = null,
                            breed = null,
                            birthDate = birthDate
                        )
                    }
                    
                    lifecycleScope.launch {
                        database.familyMemberDao().insert(member)
                    }
                } else {
                    Toast.makeText(context, "Заполните имя и дату рождения", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    
    private fun showDeleteDialog(member: FamilyMember) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить")
            .setMessage("Удалить ${member.name}?")
            .setPositiveButton("Да") { _, _ ->
                lifecycleScope.launch {
                    database.familyMemberDao().delete(member)
                    Toast.makeText(context, "${member.name} удалён", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Нет", null)
            .show()
    }
}
