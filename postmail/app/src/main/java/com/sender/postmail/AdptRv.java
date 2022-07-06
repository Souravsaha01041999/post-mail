package com.sender.postmail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdptRv extends RecyclerView.Adapter<AdptRv.MyHolder>
{
    interface clk{
        void click(String msgid,String seen);
        void longClick(String msg_id);
    }

    Context context;
    List<CaptureDatas>lst;
    clk cc;

    AdptRv(Context c,clk ccc)
    {
        this.context=c;
        this.lst=new ArrayList<>();
        this.cc=ccc;
    }
    void getData(CaptureDatas cd)
    {
        this.lst.add(cd);
    }
    void clear()
    {
        lst.clear();
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.showingallvals,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.setIsRecyclable(false);

        holder.uid.setText(lst.get(position).getUserid());
        holder.sub.setText(lst.get(position).getSubject());
        holder.dt.setText(lst.get(position).getDate()+" "+lst.get(position).getTime());

        if(lst.get(position).getSeen().equals("1"))
        {
            holder.seen.setText("");
        }
        else {
            holder.seen.setText("New");
        }
        holder.r.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                cc.longClick(lst.get(position).getMsgid());
                return false;
            }
        });
        holder.r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.seen.setText("");
                cc.click(lst.get(position).getMsgid(),lst.get(position).getSeen());
                lst.get(position).seen="1";
            }
        });
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView sub,uid,dt,seen;
        RelativeLayout r;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            sub=itemView.findViewById(R.id.rvsubject);
            uid=itemView.findViewById(R.id.rvid);
            dt=itemView.findViewById(R.id.rvdate);
            r=itemView.findViewById(R.id.rvclk);
            seen=itemView.findViewById(R.id.rvseen);
        }
    }
}
