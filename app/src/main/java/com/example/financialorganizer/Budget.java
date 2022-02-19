package com.example.financialorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class Budget extends AppCompatActivity {

    private TextView totalBudgetView;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    private DatabaseReference budgetRef, personalRef;
    private FirebaseAuth fAuth;
    private ProgressDialog loader;

    private String post_key = "";
    private String item = "";
    private int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        fAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(fAuth.getCurrentUser().getUid());
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(fAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        totalBudgetView = findViewById(R.id.totalBudgetView);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    int totalAmount = 0;

                    for(DataSnapshot snap : snapshot.getChildren()){
                        Data data = snap.getValue(Data.class);
                        totalAmount += data.getAmount();
                        String sTotal = String.valueOf("Monthly Budget: $" + totalAmount);
                        totalBudgetView.setText(sTotal);
                    }
                    int weeklyBudget = totalAmount/4;
                    int dailyBudget = totalAmount/30;
                    personalRef.child("budget").setValue(totalAmount);
                    personalRef.child("weeklyBudget").setValue(weeklyBudget);
                    personalRef.child("dailyBudget").setValue(dailyBudget);
                } else {
                    personalRef.child("budget").setValue(0);
                    personalRef.child("weeklyBudget").setValue(0);
                    personalRef.child("dailyBudget").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        getMonthlyTransportionBudgetRatios();
        getMonthlyFoodBudgetRatios();
        getMonthlyRentBudgetRatios();
        getMonthlyEntBudgetRatios();
        getMonthlyHealthBudgetRatios();
        getMonthlyOtherBudgetRatios();
    }

    private void addItem(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemSpinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetAmount = amount.getText().toString();
                String budgetItem = itemSpinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(budgetAmount)){
                    amount.setError("Amount is required!");
                    return;
                }

                if(budgetItem.equals("Select item")){
                    Toast.makeText(Budget.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                } else {
                    loader.setMessage("adding budget item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Weeks weeks = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);

                    String itemNDay = budgetItem+date;
                    String itemNWeek = budgetItem+weeks.getWeeks();
                    String itemNMonth = budgetItem+months.getMonths();

                    Data data = new Data(budgetItem, date, id, itemNDay, itemNWeek, itemNMonth, Integer.parseInt(budgetAmount), weeks.getWeeks(), months.getMonths(), null);
                    budgetRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Budget.this, "Budget item added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Budget.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Data model) {
                holder.setItemAmount("Allocated amount: $" + model.getAmount());
                holder.setDate("On: " + model.getDate());
                holder.setItemName("BudgetItem: " + model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Transportation":
                        holder.imageView.setImageResource(R.drawable.budget);
                        break;
                    case "Food":
                        holder.imageView.setImageResource(R.drawable.budget);
                        break;
                    case "Rent":
                        holder.imageView.setImageResource(R.drawable.budget);
                        break;
                    case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.budget);
                        break;
                    case "Health":
                        holder.imageView.setImageResource(R.drawable.budget);
                        break;
                    case "Other":
                        holder.imageView.setImageResource(R.drawable.budget);
                        break;
                }

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        updateData();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageView imageView;
        public TextView notes, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        public void setItemName(String itemName){

            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setItemAmount(String itemAmount){
            TextView amount = mView.findViewById(R.id.amount);
            amount.setText(itemAmount);
        }

        public void setDate(String itemDate){
            TextView date = mView.findViewById(R.id.date);
            date.setText(itemDate);
        }
    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        final TextView mItem = mView.findViewById(R.id.itemName);
        final EditText mAmount = mView.findViewById(R.id.amount);
        final EditText mNotes = mView.findViewById(R.id.note);

        mNotes.setVisibility(View.GONE);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        Button delButton = mView.findViewById(R.id.itemDelete);
        Button updateBtn = mView.findViewById(R.id.itemUpdate);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mAmount.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                String itemNDay = item + date;
                String itemNWeek = item + weeks.getWeeks();
                String itemNMonth = item + months.getMonths();

                Data data = new Data(item, date, post_key, itemNDay, itemNWeek, itemNMonth, amount, weeks.getWeeks(), months.getMonths(), null);
                budgetRef.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Budget.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Budget.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Budget.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Budget.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //TODO: Abstract these out
    private void getMonthlyTransportionBudgetRatios(){
        Query query = budgetRef.orderByChild("item").equalTo("Transportation");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int pTotal = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    //TODO: Fix how week and day are handled
                    int dayTransRatio = pTotal/30;
                    int weekTransRatio = pTotal/4;
                    int monthTransRatio = pTotal;

                    personalRef.child("dayTransRatio").setValue(dayTransRatio);
                    personalRef.child("weekTransRatio").setValue(weekTransRatio);
                    personalRef.child("monthTransRatio").setValue(monthTransRatio);

                }else {
                    personalRef.child("dayTransRatio").setValue(0);
                    personalRef.child("weekTransRatio").setValue(0);
                    personalRef.child("monthTransRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        private void getMonthlyFoodBudgetRatios(){
            Query query = budgetRef.orderByChild("item").equalTo("Food");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int pTotal = 0;
                        for (DataSnapshot ds :  snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            pTotal = Integer.parseInt(String.valueOf(total));
                        }

                        //TODO: Fix how week and day are handled
                        int dayFoodRatio = pTotal/30;
                        int weekFoodRatio = pTotal/4;
                        int monthFoodRatio = pTotal;

                        personalRef.child("dayFoodRatio").setValue(dayFoodRatio);
                        personalRef.child("weekFoodRatio").setValue(weekFoodRatio);
                        personalRef.child("monthFoodRatio").setValue(monthFoodRatio);

                    }else {
                        personalRef.child("dayFoodRatio").setValue(0);
                        personalRef.child("weekFoodRatio").setValue(0);
                        personalRef.child("monthFoodRatio").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void getMonthlyRentBudgetRatios(){
            Query query = budgetRef.orderByChild("item").equalTo("Rent");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int pTotal = 0;
                        for (DataSnapshot ds :  snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            pTotal = Integer.parseInt(String.valueOf(total));
                        }

                        //TODO: Fix how week and day are handled
                        int dayHouseRatio = pTotal/30;
                        int weekHouseRatio = pTotal/4;
                        int monthHouseRatio = pTotal;

                        personalRef.child("dayHouseRatio").setValue(dayHouseRatio);
                        personalRef.child("weekHouseRatio").setValue(weekHouseRatio);
                        personalRef.child("monthHouseRatio").setValue(monthHouseRatio);

                    }else {
                        personalRef.child("dayHouseRatio").setValue(0);
                        personalRef.child("weekHouseRatio").setValue(0);
                        personalRef.child("monthHouseRatio").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void getMonthlyEntBudgetRatios(){
            Query query = budgetRef.orderByChild("item").equalTo("Entertainment");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int pTotal = 0;
                        for (DataSnapshot ds :  snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            pTotal = Integer.parseInt(String.valueOf(total));
                        }

                        //TODO: Fix how week and day are handled
                        int dayEntRatio = pTotal/30;
                        int weekEntRatio = pTotal/4;
                        int monthEntRatio = pTotal;

                        personalRef.child("dayEntRatio").setValue(dayEntRatio);
                        personalRef.child("weekEntRatio").setValue(weekEntRatio);
                        personalRef.child("monthEntRatio").setValue(monthEntRatio);

                    }else {
                        personalRef.child("dayEntRatio").setValue(0);
                        personalRef.child("weekEntRatio").setValue(0);
                        personalRef.child("monthEntRatio").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        private void getMonthlyHealthBudgetRatios(){
            Query query = budgetRef.orderByChild("item").equalTo("Health");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int pTotal = 0;
                        for (DataSnapshot ds :  snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            pTotal = Integer.parseInt(String.valueOf(total));
                        }

                        //TODO: Fix how week and day are handled
                        int dayHealthRatio = pTotal/30;
                        int weekHealthRatio = pTotal/4;
                        int monthHealthRatio = pTotal;

                        personalRef.child("dayHealthRatio").setValue(dayHealthRatio);
                        personalRef.child("weekHealthRatio").setValue(weekHealthRatio);
                        personalRef.child("monthHealthRatio").setValue(monthHealthRatio);

                    }else {
                        personalRef.child("dayHealthRatio").setValue(0);
                        personalRef.child("weekHealthRatio").setValue(0);
                        personalRef.child("monthHealthRatio").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void getMonthlyOtherBudgetRatios(){
            Query query = budgetRef.orderByChild("item").equalTo("Other");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int pTotal = 0;
                        for (DataSnapshot ds :  snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            pTotal = Integer.parseInt(String.valueOf(total));
                        }

                        //TODO: Fix how week and day are handled
                        int dayOtherRatio = pTotal/30;
                        int weekOtherRatio = pTotal/4;
                        int monthOtherRatio = pTotal;

                        personalRef.child("dayOtherRatio").setValue(dayOtherRatio);
                        personalRef.child("weekOtherRatio").setValue(weekOtherRatio);
                        personalRef.child("monthOtherRatio").setValue(monthOtherRatio);

                    }else {
                        personalRef.child("dayOtherRatio").setValue(0);
                        personalRef.child("weekOtherRatio").setValue(0);
                        personalRef.child("monthOtherRatio").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }