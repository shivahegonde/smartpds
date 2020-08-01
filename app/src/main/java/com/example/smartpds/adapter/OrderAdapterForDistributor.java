package com.example.smartpds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.Customer;
import com.example.smartpds.R;
import com.example.smartpds.adapter.viewHolder.OrderViewHolder;
import com.example.smartpds.model.Customers;
import com.example.smartpds.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderAdapterForDistributor extends FirebaseRecyclerAdapter<Orders, OrderViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    FirebaseDatabase db;
    DatabaseReference documentReference;
    public OrderAdapterForDistributor(@NonNull FirebaseRecyclerOptions<Orders> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
        db = FirebaseDatabase.getInstance();
        documentReference = db.getReference("Customers/" + model.getCustomer());

        setShopName(model.getCustomer() , holder.shopName);
        setShopAddress(holder.shopAddress);

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

          String date=  sfd.format(new Date(model.getTimestamp()));
       holder.timestamp.setText(date);
        if (holder.amount!=null)
        holder.amount.setText(model.getTotalAmount());

    }

    private void setShopAddress(final TextView shopAddress) {

        documentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    shopAddress.setText(customer.getCity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card_layout_for_distributor, parent, false);

        return new OrderViewHolder(view);
    }

    private void setShopName(String customer, final TextView shopName) {

        shopName.setText(customer);
        documentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Customers customer = dataSnapshot.getValue(Customers.class);
                    shopName.setText(customer.getFname()+" "+customer.getLname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        documentReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists())
//                {
//                    Distributer shop = dataSnapshot.getValue(Distributer.class);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
