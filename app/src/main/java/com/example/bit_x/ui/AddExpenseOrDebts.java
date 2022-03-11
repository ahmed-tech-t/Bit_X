package com.example.bit_x.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.bit_x.R;
import com.example.bit_x.databinding.ActivityAddExpenseOrDebtsBinding;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.models.ClientModel;
import com.example.bit_x.models.ExpenseModel;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.TraderModel;
import com.example.bit_x.models.WagesModel;
import com.example.bit_x.models.WorkerModel;
import com.example.bit_x.ui.fragment.SelectTrader;
import com.example.bit_x.ui.fragment.SelectWorker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AddExpenseOrDebts extends AppCompatActivity implements View.OnClickListener , GetNameKey {
    ActivityAddExpenseOrDebtsBinding binding;
    String status="";
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    private TraderModel mainTraderModel;
    private WorkerModel mainWorkerModel;
    private WagesModel mainWagesModel;
    String projectKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityAddExpenseOrDebtsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        binding.name.setOnClickListener(this);
        binding.buAddEWSave.setOnClickListener(this);
        binding.linearLayout.setOnClickListener(this);
        binding.constraintLayout.setOnClickListener(this);
        binding.logo.setOnClickListener(this);
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();

        Intent intent = getIntent();
        projectKey =intent.getStringExtra("projectKey");
        String id = intent.getStringExtra("tag");
        switch (id){
            case "addExpense":
               setExpenseView();
                status ="addExpense";
                break;
            case "editExpense":
               binding.name.setBackgroundResource(R.drawable.layout_design_gray);
               setExpenseView();
               getExpenseData();
               getMainTrader();
               status="editExpense";
                break;
            case "addWages":
                setWagesView();
                status="addWages";
                break;
            case "editWages":
                binding.name.setBackgroundResource(R.drawable.layout_design_gray);
                setWagesView();
                getWagesData();
                getMainWorker();
                status="editWages";
                break;
            case "workerPayMethod":
                setPayMethodView();
                getWagesData();
                getMainWorker();
                status="workerPayMethod";
                break;
            case "traderPayMethod":
                setPayMethodView();
                getExpenseData();
                getMainTrader();
                status ="traderPayMethod";
                break;
        }
    }

    @Override
    public void onClick(View view) {

        try {
            int id =view.getId();
            if(binding.buAddEWSave.getId()==id){
                String desc ="";
                double value =0;
                double payed =0;
                if(binding.name.getText().length()==0){
                    binding.name.setHint("يجب ملئ هذا الحقل");
                }else binding.name.setError(null);
                if(binding.desc.getText().length()>0){
                    desc = binding.desc.getText().toString();
                }
                if(binding.value.getText().length()==0){
                    binding.value.setError("يجب ملئ هذا الحقل");
                }else {
                    value = Double.valueOf(binding.value.getText().toString());
                    binding.value.setError(null);
                }
                if(binding.payed.getText().length()==0){
                    payed =0;
                    binding.payed.setText("0");
                }else{
                    payed = Double.valueOf(binding.payed.getText().toString());
                }

                if(binding.name.getError()==null  && binding.value.getError()==null){
                    if(status.equals("addExpense")){
                        mainTraderModel.setBalance(payed-value);
                        ExpenseModel expenseModel = new ExpenseModel(mainTraderModel,desc,value,payed);
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses");
                        reference.push().setValue(expenseModel);

                        addProjectExpense();
                        addTraderBalance();
                        Intent intent = new Intent(AddExpenseOrDebts.this, Expense.class);
                        intent.putExtra("projectKey",projectKey);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("editExpense")){
                        Intent intent1 =getIntent();
                        String expenseKey = intent1.getStringExtra("expenseKey");
                        editProjectExpense();
                        editTraderBalance();
                        ExpenseModel expenseModel = new ExpenseModel(mainTraderModel,desc,value,payed);
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
                        reference.setValue(expenseModel);

                        Intent intent = new Intent(AddExpenseOrDebts.this, Expense.class);
                        intent.putExtra("projectKey",projectKey);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("addWages")){
                        mainWorkerModel.setBalance(payed-value);
                        WagesModel wagesModel = new WagesModel(mainWorkerModel,desc,value,payed);
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages");
                        reference.push().setValue(wagesModel);
                        addProjectExpense();
                        addWorkerBalance();
                        Intent intent = new Intent(AddExpenseOrDebts.this, Wages.class);
                        intent.putExtra("projectKey",projectKey);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("editWages")){
                        Intent intent1 =getIntent();
                        String wagesKey = intent1.getStringExtra("wagesKey");
                        editProjectWages();
                        editWorkerBalance();
                        WagesModel wagesModel = new WagesModel(mainWorkerModel,desc,value,payed);
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
                        reference.setValue(wagesModel);
                        Intent intent = new Intent(AddExpenseOrDebts.this, Wages.class);
                        intent.putExtra("projectKey",projectKey);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("workerPayMethod")){
                        Intent intent1 = getIntent();
                        String wagesKey = intent1.getStringExtra("wagesKey");
                        payWorkerMethod();
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    double oldPayed = snapshot.getValue(WagesModel.class).getPayed();
                                    snapshot.getRef().child("payed").setValue(Double.valueOf(binding.payed.getText().toString())+oldPayed);
                                    double oldDebts =snapshot.getValue(WagesModel.class).getDebts();
                                    snapshot.getRef().child("debts").setValue(Double.valueOf(binding.payed.getText().toString())+oldDebts);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Intent intent = new Intent(AddExpenseOrDebts.this, Wages.class);
                        intent.putExtra("projectKey",projectKey);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("traderPayMethod")){
                        Intent intent1 = getIntent();
                        String expenseKey = intent1.getStringExtra("expenseKey");
                        payTraderMethod();
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    double oldPayed = snapshot.getValue(ExpenseModel.class).getPayed();
                                    snapshot.getRef().child("payed").setValue(Double.valueOf(binding.payed.getText().toString())+oldPayed);
                                    double oldDebts =snapshot.getValue(ExpenseModel.class).getDebts();
                                    snapshot.getRef().child("debts").setValue(Double.valueOf(binding.payed.getText().toString())+oldDebts);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        Intent intent = new Intent(AddExpenseOrDebts.this, Expense.class);
                        intent.putExtra("projectKey",projectKey);
                        startActivity(intent);
                        finish();
                    }
                }

            }else if(binding.name.getId()==id){
                if(status.equals("addExpense")){
                    binding.scrollView.setVisibility(View.GONE);
                    binding.fragmentContainer.setVisibility(View.VISIBLE);
                    Fragment fragment = new SelectTrader();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();

                }else if(status.equals("addWages")){
                    binding.scrollView.setVisibility(View.GONE);
                    binding.fragmentContainer.setVisibility(View.VISIBLE);
                    Fragment fragment = new SelectWorker();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
                }
            }else if(id==binding.logo.getId()||id==binding.linearLayout.getId()||id==binding.constraintLayout.getId()){
                hideKeyBoard(view);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeFragment(){
        FragmentManager fragmentManager ;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if(fragment!=null){
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();}
    }
    @Override
    public void getNameKey(String key) {
        if(status.equals("addExpense")){
            closeFragment();
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders").child(key);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists()){
                       TraderModel traderModel = snapshot.getValue(TraderModel.class);
                       traderModel.setKey(snapshot.getKey());
                       binding.name.setText(traderModel.getName());
                       mainTraderModel = traderModel;
                   }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else if (status.equals("addWages")){
            closeFragment();
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(key);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        WorkerModel workerModel = snapshot.getValue(WorkerModel.class);
                        workerModel.setKey(snapshot.getKey());
                        binding.name.setText(workerModel.getName());
                        mainWorkerModel = workerModel;
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        binding.fragmentContainer.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.VISIBLE);
    }
    private void getProjectName(){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   String name =snapshot.getValue(ProjectModel.class).getName();
                   binding.projectName.setText(name);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setExpenseView(){
        getProjectName();
        binding.name.setHint(R.string.traderName);
        binding.desc.setHint(R.string.productDescription);
        binding.value.setHint(R.string.value);
        binding.payed.setHint(R.string.payed);
    }
    private void getExpenseData(){
        Intent intent =getIntent();
        String expenseKey = intent.getStringExtra("expenseKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   ExpenseModel expenseModel = snapshot.getValue(ExpenseModel.class);
                   expenseModel.setKey(snapshot.getKey());
                   mainTraderModel = expenseModel.getTraderModel();
                   binding.name.setText(expenseModel.getTraderModel().getName());
                   binding.desc.setText(expenseModel.getDescription());
                   binding.value.setText(String.valueOf(expenseModel.getValue()));
                   if(status.equals("editExpense")) binding.payed.setText(String.valueOf(expenseModel.getPayed()));
                   else if(status.equals("traderPayMethod")) {
                       if(expenseModel.getDebts()==0){
                           binding.payed.setHint(R.string.theirIsNoDebts);
                       }else if(expenseModel.getDebts()<0){
                           binding.payed.setHint(  "المديونية"+ " :   " + expenseModel.getDebts()*-1 );
                       }else if(expenseModel.getDebts()>0){
                           binding.payed.setHint( "الرصيد" + " :   " + expenseModel.getDebts());
                       }
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addProjectExpense(){
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 double expense = Double.parseDouble(binding.value.getText().toString());
                 double oldExpense = snapshot.getValue(ProjectModel.class).getExpense();
                 snapshot.getRef().child("expense").setValue(expense+oldExpense);
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void editProjectExpense(){
        Intent intent =getIntent();
        String expenseKey = intent.getStringExtra("expenseKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists()){
                  double oldExpense =snapshot.getValue(ExpenseModel.class).getValue();
                  reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                  reference.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if (snapshot.exists()){
                              double projectOldExpense = snapshot.getValue(ProjectModel.class).getExpense();
                              double newExpense = Double.parseDouble(binding.value.getText().toString());
                              snapshot.getRef().child("expense").setValue(projectOldExpense-oldExpense+newExpense);
                          }
                      }
                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void addTraderBalance(){
        double debts = Double.parseDouble(binding.payed.getText().toString())-Double.parseDouble(binding.value.getText().toString());
        String traderKey =mainTraderModel.getKey();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders").child(traderKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double oldBalance = snapshot.getValue(TraderModel.class).getBalance();
                   snapshot.getRef().child("balance").setValue(debts+oldBalance);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void editTraderBalance(){
        Intent intent =getIntent();
        String expenseKey = intent.getStringExtra("expenseKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   ExpenseModel expenseModel =snapshot.getValue(ExpenseModel.class);
                   double dept = expenseModel.getValue()-expenseModel.getPayed();
                   String traderKey = expenseModel.getTraderModel().getKey();
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders").child(traderKey);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               double traderBalance =snapshot.getValue(TraderModel.class).getBalance();
                               double value = Double.valueOf(binding.value.getText().toString());
                               double payed = Double.valueOf(binding.payed.getText().toString());
                               double newDebt = payed-value;
                               snapshot.getRef().child("balance").setValue(traderBalance+dept+newDebt);
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               double traderBalance =snapshot.getValue(ExpenseModel.class).getTraderModel().getBalance();
                               double value = Double.valueOf(binding.value.getText().toString());
                               double payed = Double.valueOf(binding.payed.getText().toString());
                               double newDebt = payed-value;
                               snapshot.getRef().child("traderModel").child("balance").setValue(traderBalance+dept+newDebt);
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getMainTrader(){
        Intent intent1 =getIntent();
        String expenseKey = intent1.getStringExtra("expenseKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mainTraderModel =snapshot.getValue(ExpenseModel.class).getTraderModel();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //***************************
    private void setWagesView(){
        getProjectName();
        binding.name.setHint(R.string.workerName);
        binding.desc.setHint(R.string.description);
        binding.value.setHint(R.string.value);
        binding.payed.setHint(R.string.payed);
    }
    private void getMainWorker(){
        Intent intent1 =getIntent();
        String wagesKey = intent1.getStringExtra("wagesKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mainWorkerModel =snapshot.getValue(WagesModel.class).getWorkerModel();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getWagesData(){
        Intent intent =getIntent();
        String wagesKey = intent.getStringExtra("wagesKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   WagesModel wagesModel = snapshot.getValue(WagesModel.class);
                   wagesModel.setKey(snapshot.getKey());
                   mainWorkerModel = wagesModel.getWorkerModel();
                   mainWagesModel =wagesModel;
                   binding.name.setText(wagesModel.getWorkerModel().getName());
                   binding.desc.setText(wagesModel.getDescription());
                   binding.value.setText(String.valueOf(wagesModel.getValue()));

                   if(status.equals("editWages")) binding.payed.setText(String.valueOf(wagesModel.getPayed()));
                   else if(status.equals("workerPayMethod")){
                       if(wagesModel.getDebts()==0){
                           binding.payed.setHint(R.string.theirIsNoDebts);
                       }else if(wagesModel.getDebts()<0){
                           binding.payed.setHint(  "المديونية"+ " :   " + wagesModel.getDebts()*-1 );
                       }else if(wagesModel.getDebts()>0){
                           binding.payed.setHint( "الرصيد" + " :   " + wagesModel.getDebts());
                       }
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void addWorkerBalance(){
        double debts = Double.parseDouble(binding.payed.getText().toString())-Double.parseDouble(binding.value.getText().toString());
        String workerKey =mainWorkerModel.getKey();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double oldBalance = snapshot.getValue(WorkerModel.class).getBalance();
                   snapshot.getRef().child("balance").setValue(debts+oldBalance);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void editProjectWages(){
        Intent intent =getIntent();
        String wagesKey = intent.getStringExtra("wagesKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double oldWages = snapshot.getValue(WagesModel.class).getValue();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                double projectOldExpense = snapshot.getValue(ProjectModel.class).getExpense();
                                double newExpense = Double.parseDouble(binding.value.getText().toString());
                                snapshot.getRef().child("expense").setValue(projectOldExpense-oldWages+newExpense);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void editWorkerBalance(){
        Intent intent =getIntent();
        String wagesKey = intent.getStringExtra("wagesKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  WagesModel wagesModel =snapshot.getValue(WagesModel.class);
                  double dept = wagesModel.getValue()-wagesModel.getPayed();
                  String workerKey = wagesModel.getWorkerModel().getKey();
                  reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey);
                  reference.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            double workerBalance =snapshot.getValue(WorkerModel.class).getBalance();
                            double value = Double.valueOf(binding.value.getText().toString());
                            double payed = Double.valueOf(binding.payed.getText().toString());
                            double newDebt = payed-value;
                            snapshot.getRef().child("balance").setValue(workerBalance+dept+newDebt);
                        }
                      }
                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });
                  reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
                  reference.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if (snapshot.exists()){
                             double workerBalance =snapshot.getValue(WagesModel.class).getWorkerModel().getBalance();
                             double value = Double.valueOf(binding.value.getText().toString());
                             double payed = Double.valueOf(binding.payed.getText().toString());
                             double newDebt = payed-value;
                             snapshot.getRef().child("workerModel").child("balance").setValue(workerBalance+dept+newDebt);
                         }
                      }
                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });

              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void payWorkerMethod(){
        Intent intent =getIntent();
        String wagesKey = intent.getStringExtra("wagesKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages").child(wagesKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double newPayed =Double.parseDouble(binding.payed.getText().toString());
                   double workerOldBalance = snapshot.getValue(WagesModel.class).getWorkerModel().getBalance();
                   String workerKey = snapshot.getValue(WagesModel.class).getWorkerModel().getKey();
                   snapshot.getRef().child("workerModel").child("balance").setValue(workerOldBalance+newPayed);
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               double newPayed =Double.parseDouble(binding.payed.getText().toString());
                               double workerOldBalance = snapshot.getValue(WorkerModel.class).getBalance();
                               snapshot.getRef().child("balance").setValue(workerOldBalance+newPayed);
                           }

                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void payTraderMethod(){
        Intent intent =getIntent();
        String expenseKey = intent.getStringExtra("expenseKey");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Expenses").child(expenseKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double newPayed =Double.parseDouble(binding.payed.getText().toString());
                   double traderOldBalance = snapshot.getValue(ExpenseModel.class).getTraderModel().getBalance();
                   String traderKey = snapshot.getValue(ExpenseModel.class).getTraderModel().getKey();
                   snapshot.getRef().child("traderModel").child("balance").setValue(traderOldBalance+newPayed);
                   reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders").child(traderKey);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               double newPayed =Double.parseDouble(binding.payed.getText().toString());
                               double traderOldBalance = snapshot.getValue(TraderModel.class).getBalance();
                               snapshot.getRef().child("balance").setValue(traderOldBalance+newPayed);
                           }

                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       try {
           if(status.equals("addExpense")){
               Intent intent = new Intent(AddExpenseOrDebts.this, Expense.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }else if(status.equals("editExpense")){
               Intent intent = new Intent(AddExpenseOrDebts.this, Expense.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }else if(status.equals("addWages")){
               Intent intent = new Intent(AddExpenseOrDebts.this, Wages.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }else if(status.equals("editWages")){
               Intent intent = new Intent(AddExpenseOrDebts.this, Wages.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }else if(status.equals("workerPayMethod")){
               Intent intent = new Intent(AddExpenseOrDebts.this, Wages.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }
       }catch (Exception e){
           e.printStackTrace();
       }

    }
    private void setPayMethodView(){
        getProjectName();
        binding.name.setBackgroundResource(R.drawable.layout_design_gray);
        binding.value.setBackgroundResource(R.drawable.layout_design_gray);
        binding.desc.setBackgroundResource(R.drawable.layout_design_gray);

        binding.name.setFocusable(false);
        binding.name.setFocusableInTouchMode(false);
        binding.name.setClickable(false);
        binding.name.setCursorVisible(false);

        binding.desc.setFocusable(false);
        binding.desc.setFocusableInTouchMode(false);
        binding.desc.setClickable(false);
        binding.desc.setCursorVisible(false);

        binding.value.setFocusable(false);
        binding.value.setFocusableInTouchMode(false);
        binding.value.setClickable(false);
        binding.value.setCursorVisible(false);

    }
    public void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void setAppLanguage(){
        String languageToLoad = "english"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}