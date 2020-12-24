package com.undispuated.alertsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.undispuated.alertsystem.R
import com.undispuated.alertsystem.database.ContactsEntity
import com.undispuated.alertsystem.model.Contact

class ContactsAdapter(val context: Context, var contactList: List<ContactsEntity>): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    class ContactsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rlContainer: RelativeLayout = view.findViewById(R.id.rlContainer)
        val imgGenderPic: ImageView = view.findViewById(R.id.imgGenderPic)
        val txtNickname: TextView = view.findViewById(R.id.txtNickname)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtPhoneNum: TextView = view.findViewById(R.id.txtPhoneNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_single_row,parent,false)

        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val listContacts = arrayListOf<Contact>()

        for(i in contactList){
            listContacts.add(
                Contact(
                    i.isMale,
                    i.origName,
                    i.nickname,
                    i.phoneNum
                )
            )
        }

        val contact = listContacts[position]

        holder.txtNickname.text = contact.nickname
        holder.txtName.text = contact.origName
        holder.txtPhoneNum.text = context.getString(R.string.str_phone_number, contact.phoneNum)

        if(contact.isMale)
            holder.imgGenderPic.setImageResource(R.drawable.icon_male)
        else
            holder.imgGenderPic.setImageResource(R.drawable.icon_female)

        holder.rlContainer.setOnClickListener {
            //Implement dialog box appearing from bottom
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

}