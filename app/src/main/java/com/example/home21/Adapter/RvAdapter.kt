package Adapter

import Model.Contact
import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.home21.R
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.github.florent37.runtimepermission.kotlin.askPermission

import kotlinx.android.synthetic.main.item_rv.view.*

class RvAdapter(var context: Context, var list: ArrayList<Contact>, var itemClick: ItemClick) :
    RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    inner class MyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(model: Contact, position: Int) {

            itemView.txt_name.text = list[position].name
            itemView.txt_number.text = list[position].number

           val nam= itemView.txt_number.text


         itemView.sms_imga.setOnClickListener {
             var smsNumberUri= Uri.parse("sms:${ nam.toString()}")
             val smsIntent=Intent(Intent.ACTION_SENDTO, smsNumberUri)
             startActivity(context,smsIntent,bundleOf())

             Toast.makeText(context, "clickded sms", Toast.LENGTH_SHORT).show()

         }
            itemView.img_call.setOnClickListener {
                itemClick.call(list, position, model)

                val phonNumber = list[position].number
                val intent = Intent(Intent(Intent.ACTION_CALL))
                intent.data = Uri.parse("tel:$phonNumber")
                startActivity(context,intent, bundleOf())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface ItemClick {
    fun call(list: ArrayList<Contact>, position: Int, rvModel: Contact)
}

