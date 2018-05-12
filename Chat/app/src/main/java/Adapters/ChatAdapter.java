package Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.tut.lamh.chat.R;
import Model.ObjectClass.RowChat;

/**
 * Created by huynh on 12/11/2017.
 */

public class ChatAdapter extends ArrayAdapter<RowChat> {

    @NonNull Context context;
    @LayoutRes int resource;
    @NonNull ArrayList<RowChat> objects;

    public ChatAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<RowChat> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.context);

        View row = inflater.inflate(this.resource,null);

        LinearLayout containtChat = row.findViewById(R.id.containtChat);
        TextView txtvUserChat = row.findViewById(R.id.txtvUserChat);
        TextView txtvNoiDungChat = row.findViewById(R.id.txtvNoiDungChat);
        ImageView imgvHinhChat = row.findViewById(R.id.imgvHinhChat);

        txtvUserChat.setText(objects.get(position).getUsernameChat().toString());

        if(objects.get(position).getNoiDungChat().toString()!="")
        {
            txtvNoiDungChat.setText(objects.get(position).getNoiDungChat().toString());
            txtvNoiDungChat.setVisibility(View.VISIBLE);
        }

        if(objects.get(position).getHinhChat()!=null)
        {
            imgvHinhChat.setImageDrawable(objects.get(position).getHinhChat());
            imgvHinhChat.setVisibility(View.VISIBLE);
        }

        //Set vitri cho containt chat
        if(objects.get(position).isLaTui())
        {
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.gravity = Gravity.RIGHT;

            containtChat.setLayoutParams(params);
        }

        return row;
    }
}
