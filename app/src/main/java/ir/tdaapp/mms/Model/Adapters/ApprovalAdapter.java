package ir.tdaapp.mms.Model.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.R;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.MyViewHolder> {

    Context context;
    List<VM_Approvals> vals;

    public ApprovalAdapter(Context context) {
        this.context = context;
        vals=new ArrayList<>();
    }

    public void Add(VM_Approvals vm_approval){
        vals.add(vm_approval);
        notifyItemInserted(vals.size());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_approval,parent,false);
        return new MyViewHolder(view);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lbl_Title=itemView.findViewById(R.id.lbl_Title);
        }
    }

}
