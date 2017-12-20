package com.example.sdvii.incomemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.libRG.CustomTextView;

import java.util.ArrayList;

/**
 * Created by houma on 17-Dec-17.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnLongClickListener {

    ArrayList<DataModel> dataSet;
    Context mContext;
    ListFragment frg;
    DatabaseHandler db = new DatabaseHandler(getContext());


    private static class ViewHolder {
        CustomTextView ctvDate;
        TextView tvMoney;
        TextView tvTitle;
        TextView tvType;
    }

    public CustomAdapter (ArrayList<DataModel> data, Context context, ListFragment frg)
    {
        super(context,R.layout.row_item,data);
        this.dataSet = data;
        this.mContext = context;
        this.frg = frg;
    }


    @Override
    public boolean onLongClick(View v) {

        ViewHolder vh = (ViewHolder) v.getTag();
        int position=(Integer) vh.tvMoney.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;
        showChangeLangDialog(dataModel.getdId());
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataModel dataModel = getItem(position);
        ViewHolder vh;

        final View result;

        if (convertView == null){
            vh = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item,parent,false);
            vh.ctvDate = convertView.findViewById(R.id.ctvDate);
            vh.tvMoney = convertView.findViewById(R.id.tvMoney);
            vh.tvTitle = convertView.findViewById(R.id.tvTitle);
            vh.tvType = convertView.findViewById(R.id.tvType);

            result = convertView;
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        vh.ctvDate.setText(dataModel.getDate());
        vh.tvTitle.setText(dataModel.getTitle());
        vh.tvType.setText(dataModel.getType());
        vh.tvMoney.setText(dataModel.getMoney());
        vh.tvMoney.setTag(position);
        result.setOnLongClickListener(this);



        return convertView;
    }

    public void showChangeLangDialog(final int id) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.two_button_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button btnEdit = dialogView.findViewById(R.id.btnEdit);
        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Delete",Toast.LENGTH_SHORT).show();
                deletePaymentItem(id);
                b.dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Edit",Toast.LENGTH_SHORT).show();
                editPaymentItem(id);
                b.dismiss();
            }
        });

    }

    public void deletePaymentItem(int id){
        db.deletePaymentById(id);
        frg.refresh();
    }

    public void editPaymentItem(int id){
        MainActivity ma = (MainActivity) frg.getActivity();
        ma.editPaymentActvity(id);
    }

}
