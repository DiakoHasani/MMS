package ir.tdaapp.mms.Model.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.R;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    List<VM_Requests> vals;
    Context context;

    public RequestAdapter(Context context){
        vals=new ArrayList<>();
        this.context=context;
    }

    //در اینجا یک درخواست جدید اضافه می شود
    public void Add(VM_Requests vm_request){
        vals.add(vm_request);
        notifyItemInserted(vals.size());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_request,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (vals.get(position).getTitle().length() <= 30)
            holder.lbl_Title.setText(Html.fromHtml(vals.get(position).getTitle()));
        else
            holder.lbl_Title.setText(Html.fromHtml(vals.get(position).getTitle().substring(0, 30) + "..."));

        //در اینجا چک می کند که این درخواست در چه وضعیتی است تا آیکون مربوط به آن را ست کند
        switch (vals.get(position).getCondition()){
            case Accepted:
                holder.img_Condition.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_accepted));
                break;
            case Waiting:
                holder.img_Condition.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_waiting));
                break;
            case Reject:
                holder.img_Condition.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reject));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return vals.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_Title;
        ImageView img_Condition;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lbl_Title=itemView.findViewById(R.id.lbl_Title);
            img_Condition=itemView.findViewById(R.id.img_Condition);
        }
    }

}
