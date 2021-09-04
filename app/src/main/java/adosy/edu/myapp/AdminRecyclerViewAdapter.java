package adosy.edu.myapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<AdminRecyclerViewAdapter.ViewHolder> {

    private List<String> mDataName;
    private List<String> mDataPhone;
    private List<String> mDataMail;
    private List<String> mDataLocation;
    private List<String> mDataDate;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    Context context_global;

    // data is passed into the constructor
    AdminRecyclerViewAdapter(Context context, List<String> Name, List<String> Phone, List<String> Mail, List<String> Location, List<String> Date) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataName = Name;
        this.mDataPhone = Phone;
        this.mDataMail = Mail;
        this.mDataLocation = Location;
        this.mDataDate = Date;
        context_global = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.admin_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mDataName.get(position);
        String phone = mDataPhone.get(position);
        String mail = mDataMail.get(position);
        String location = mDataLocation.get(position);
        String date = mDataDate.get(position);

        holder.myTextViewName.setText(name);
        holder.myTextViewPhone.setText(phone);
        holder.myTextViewMail.setText(mail);
        holder.myTextViewLocation.setText(location);
        holder.myTextViewDate.setText(date);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mDataName.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewName, myTextViewPhone, myTextViewMail, myTextViewLocation, myTextViewDate;


        ViewHolder(View itemView) {
            super(itemView);
            myTextViewName = itemView.findViewById(R.id.tvRowName);
            myTextViewPhone = itemView.findViewById(R.id.tvRowPhone);
            myTextViewMail = itemView.findViewById(R.id.tvRowMail);
            myTextViewLocation = itemView.findViewById(R.id.tvRowLocation);
            myTextViewDate = itemView.findViewById(R.id.tvRowDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mDataName.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}