package ir.tdaapp.mms.Model.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.R;

//آداپتر مربوط به جلسات
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> {

    List<VM_Meetings> vals;
    Context context;

    public MeetingAdapter(Context context) {
        this.context = context;
        vals = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_meeting, parent, false);
        return new MyViewHolder(view);
    }

    //در اینجا یک آیتم جدید به آداپتر اضافه می شود
    public void Add(VM_Meetings meeting) {
        vals.add(meeting);
        notifyItemInserted(vals.size());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (vals.get(position).getTitle().length() <= 30)
            holder.lbl_Title.setText(vals.get(position).getTitle());
        else
            holder.lbl_Title.setText(vals.get(position).getTitle().substring(0, 30) + "...");
    }

    @Override
    public int getItemCount() {
        return vals.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_Title;
        ImageView img_add;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lbl_Title = itemView.findViewById(R.id.lbl_Title);
            img_add = itemView.findViewById(R.id.img_add);
        }
    }

}
