package com.example.bit_x.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.bit_x.R;
import com.example.bit_x.databinding.ActivityAddTraderOrWorkerBinding;
import com.example.bit_x.interfaces.GetNameKey;
import com.example.bit_x.models.ClientModel;
import com.example.bit_x.models.IncomeModel;
import com.example.bit_x.models.ProjectModel;
import com.example.bit_x.models.TraderModel;
import com.example.bit_x.models.WorkerModel;
import com.example.bit_x.ui.fragment.SelectClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTraderOrWorker extends AppCompatActivity implements View.OnClickListener , GetNameKey {
    ActivityAddTraderOrWorkerBinding binding;
    String status="";
    private DatabaseReference databaseReference;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    private ClientModel mainClientModel;
    private String projectKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAddTraderOrWorkerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);
        setAppLanguage();
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();


        binding.logo.setOnClickListener(this);
        binding.mainConstraintLayout.setOnClickListener(this);
        binding.constraintLayout.setOnClickListener(this);
        binding.secondField.setOnClickListener(this);
        binding.buAddTWSave.setOnClickListener(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("tag");
        projectKey =intent.getStringExtra("projectKey");
        switch (id){
            case "addTrader":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders");
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.traderName);
                binding.secondField.setHint(R.string.mobilePhone);
                status ="addTrader";
            break;
            case "editTrader":
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.traderName);
                binding.secondField.setHint(R.string.mobilePhone);
                Intent intent1 = getIntent();
                String traderKey = intent1.getStringExtra("traderKey");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Traders").child(traderKey);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        TraderModel traderModel = snapshot.getValue(TraderModel.class);
                        binding.firstField.setText(traderModel.getName());
                        binding.secondField.setText(traderModel.getPhoneNumber());
                    }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                status ="editTrader";
                break;
            case "addWorker":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers");
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.workerName);
                binding.secondField.setHint(R.string.mobilePhone);
                status ="addWorker";
                break;
            case "editWorker":
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.workerName);
                binding.secondField.setHint(R.string.mobilePhone);

                String workerKey = intent.getStringExtra("workerKey");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (snapshot.exists()){
                          WorkerModel workerModel = snapshot.getValue(WorkerModel.class);
                          binding.firstField.setText(workerModel.getName());
                          binding.secondField.setText(workerModel.getPhoneNumber());
                      }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                status ="editWorker";
                break;
            case "addIncome":
                binding.projectName.setVisibility(View.VISIBLE);
                binding.firstField.setHint(R.string.description);
                binding.secondField.setHint(R.string.value);
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (snapshot.exists()){
                          ProjectModel projectModel =snapshot.getValue(ProjectModel.class);
                          binding.projectName.setText(projectModel.getName());
                      }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Incomes");
                status ="addIncome";
                break;
            case "editIncome":
                String incomeKey = intent.getStringExtra("incomeKey");
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.description);
                binding.secondField.setHint(R.string.value);
                System.out.println(incomeKey);
                System.out.println(projectKey);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Incomes").child(incomeKey);
               databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()) {
                           IncomeModel incomeModel = snapshot.getValue(IncomeModel.class);
                           binding.firstField.setText(incomeModel.getName());
                           binding.secondField.setText(String.valueOf(incomeModel.getBalance()));
                       }else System.out.println("error");
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

                status ="editIncome";
                break;
            case "addClient":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients");
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.clientName);
                binding.secondField.setHint(R.string.mobilePhone);
                status ="addClient";
                break;
            case "editClient":
                String clientKey = intent.getStringExtra("clientKey");
                binding.projectName.setVisibility(View.GONE);
                binding.firstField.setHint(R.string.clientName);
                binding.secondField.setHint(R.string.mobilePhone);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients").child(clientKey);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           ClientModel clientModel = snapshot.getValue(ClientModel.class);
                           binding.firstField.setText(clientModel.getName());
                           binding.secondField.setText(clientModel.getPhoneNumber());
                       }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                status ="editClient";
                break;
            case "addProject":
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects");
                setProjectView();
                status ="addProject";
                break;
            case "editProject":
                setProjectView();
                binding.secondField.setBackgroundResource(R.drawable.layout_design_gray);
                getMainClientModel();
                status ="editProject";
                break;
            case "editTraderDebts":
                status="editTraderDebts";
                break;
            case "editWorkerDebts":
                binding.projectName.setVisibility(View.VISIBLE);
                binding.firstField.setFocusable(false);
                binding.firstField.setFocusableInTouchMode(false);
                binding.firstField.setClickable(false);
                binding.firstField.setCursorVisible(false);
                binding.secondField.setHint(R.string.value);
                binding.firstField.setBackgroundResource(R.drawable.layout_design_gray);
                String workerKey1 = intent.getStringExtra("workerKey");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey1);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (snapshot.exists()){
                          WorkerModel workerModel = snapshot.getValue(WorkerModel.class);
                          binding.firstField.setText(workerModel.getName());
                          binding.secondField.setHint(workerModel.getBalance()*-1+"  : المديونية");
                      }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                status="editWorkerDebts";
                break;
        }
    }
    @Override
    public void onClick(View view) {

     try {
         if (view.getId() == binding.buAddTWSave.getId()) {
             String firstField = binding.firstField.getText().toString();
             String secondField = binding.secondField.getText().toString();
             if (firstField.length() == 0) {
                 binding.firstField.setError("يجب ملئ هذا الحقل");
             } else binding.firstField.setError(null);
             if(status.equals("addProject")||status.equals("editProject")){
                 if (secondField.length()==0){
                     binding.secondField.setError("يجب ملئ هذا الحقل");
                 }else binding.secondField.setError(null);
             }

             if (binding.firstField.getError() == null && binding.secondField.getError()==null) {
                 if (status.equals("addTrader")) {
                     TraderModel traderModel = new TraderModel(firstField, secondField);
                     databaseReference.push().setValue(traderModel);

                     Intent intent = new Intent(AddTraderOrWorker.this, Traders.class);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("addWorker")) {
                     WorkerModel workerModel = new WorkerModel(firstField, secondField);
                     databaseReference.push().setValue(workerModel);
                     Intent intent = new Intent(AddTraderOrWorker.this, Workers.class);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("addIncome")) {
                     String date = getCurrentTime();
                     double income =Double.valueOf(secondField);
                     IncomeModel incomeModel = new IncomeModel(firstField,income , date);
                     databaseReference.push().setValue(incomeModel);
                     databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);

                     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if (snapshot.exists()){
                                 double t = snapshot.getValue(ProjectModel.class).getIncome();
                                 double t1 = Double.valueOf(binding.secondField.getText().toString());
                                 snapshot.getRef().child("income").setValue(t+t1);
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });

                     controlClientBalance(projectKey,Double.valueOf(secondField),"+",0);
                     controlClientBalanceInProject(projectKey,Double.valueOf(secondField),"+",0);

                     Intent intent = new Intent(AddTraderOrWorker.this, Income.class);
                     intent.putExtra("projectKey",projectKey);
                     startActivity(intent);
                     finish();
                 }else if (status.equals("editIncome")) {
                     Intent intent1 =getIntent();
                     String incomeKey =intent1.getStringExtra("incomeKey");
                     editProjectIncome(projectKey,incomeKey);
                     double newIncomeBalance = Double.parseDouble(binding.secondField.getText().toString());
                     editClientIncome(projectKey,incomeKey,newIncomeBalance);

                     //to save
                     databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Incomes").child(incomeKey);
                     IncomeModel incomeModel = new IncomeModel(firstField, Double.valueOf(secondField),getCurrentTime());
                     databaseReference.setValue(incomeModel);

                     Intent intent = new Intent(AddTraderOrWorker.this, Income.class);
                     intent.putExtra("projectKey",projectKey);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("editTrader")) {
                     TraderModel traderModel = new TraderModel(firstField, secondField);
                     databaseReference.setValue(traderModel);
                     Intent intent = new Intent(AddTraderOrWorker.this, Traders.class);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("editWorker")) {
                     WorkerModel workerModel = new WorkerModel(firstField, secondField);
                     databaseReference.setValue(workerModel);
                     Intent intent = new Intent(AddTraderOrWorker.this, Workers.class);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("addClient")) {
                     ClientModel clientModel = new ClientModel(firstField, secondField);
                     databaseReference.push().setValue(clientModel);
                     Intent intent = new Intent(AddTraderOrWorker.this, Clients.class);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("editClient")) {
                     ClientModel clientModel = new ClientModel(firstField, secondField);
                     databaseReference.setValue(clientModel);
                     Intent intent = new Intent(AddTraderOrWorker.this, Clients.class);
                     startActivity(intent);
                     finish();
                 } else if (status.equals("addProject")) {
                     databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects");
                     ProjectModel projectModel = new ProjectModel(firstField, mainClientModel);
                     databaseReference.push().setValue(projectModel);
                     Intent intent = new Intent(AddTraderOrWorker.this, Projects.class);
                     startActivity(intent);
                     finish();
                 }else if (status.equals("editProject")) {
                     databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("clientModel");
                     databaseReference.setValue(mainClientModel);
                     databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if (snapshot.exists()){
                                 snapshot.getRef().child("name").setValue(binding.firstField.getText().toString());
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });

                     Intent intent = new Intent(AddTraderOrWorker.this, Projects.class);
                     startActivity(intent);
                     finish();
                 }else if(status.equals("editWorkerDebts")){
                     Intent intent =getIntent();
                     String workerKey = intent.getStringExtra("workerKey");
                     databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Workers").child(workerKey);
                     databaseReference.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if (snapshot.exists()){
                                 double balance = snapshot.getValue(WorkerModel.class).getBalance();
                                 snapshot.getRef().child("balance").setValue(secondField+balance);
                                 databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Wages");
                             }
                         }
                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {
                         }
                     });


                 }
             }
         } else if (binding.secondField.getId() == view.getId()) {
             if (status == "addProject") {
                 binding.constraintLayout.setVisibility(View.GONE);
                 Fragment fragment = new SelectClient();
                 FragmentManager fragmentManager = getSupportFragmentManager();
                 fragmentManager.beginTransaction().add(R.id.clientFragmentContainer, fragment).commit();
             }
         }else if(view.getId()==binding.logo.getId()||view.getId()==binding.mainConstraintLayout.getId()||view.getId()==binding.constraintLayout.getId()){
             hideKeyBoard(view);
         }
     }catch (Exception e){
         e.printStackTrace();
     }
    }


    @Override
    public void getNameKey(String key) {
        closeFragment();
        binding.constraintLayout.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ClientModel clientModel = snapshot.getValue(ClientModel.class);
                    clientModel.setKey(snapshot.getKey());
                    binding.secondField.setText(clientModel.getName());
                    mainClientModel = clientModel;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private String getCurrentTime(){
        SimpleDateFormat time  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = time.format(new Date());
        return currentDate;
    }
    public void closeFragment(){
        FragmentManager fragmentManager ;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.clientFragmentContainer);
        if(fragment!=null){
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();}
    }
    private void controlClientBalanceInProject(String keyProject ,double incomeNewBalance ,String operation ,double incomeOldBalance){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(keyProject).child("clientModel");
        databaseReference .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists()){
                  double clientOldBalance = snapshot.getValue(ClientModel.class).getBalance();
                  double totalNewBalance=0;
                  if(operation.equals("+")){
                      totalNewBalance = incomeNewBalance + clientOldBalance;
                  }else if(operation.equals("-")){
                      totalNewBalance = clientOldBalance-incomeNewBalance;
                  }else if(operation.equals("/")){//edit
                      totalNewBalance = clientOldBalance - incomeOldBalance + incomeNewBalance;
                  }
                  snapshot.getRef().child("balance").setValue(totalNewBalance);
              }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void controlClientBalance(String keyProject,double incomeNewBalance ,String operation,double incomeOldBalance) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(keyProject).child("clientModel");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   ClientModel clientModel = snapshot.getValue(ClientModel.class);
                   String key = clientModel.getKey();
                   databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Clients").child(key);
                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if (snapshot.exists()){
                             if(snapshot.exists()){
                                 double clientOldBalance = snapshot.getValue(ClientModel.class).getBalance();
                                 double totalNewBalance = 0;
                                 if (operation == "+") {
                                     totalNewBalance = incomeNewBalance + clientOldBalance;
                                 } else if (operation == "-") {
                                     totalNewBalance = clientOldBalance - incomeNewBalance;
                                 }else if(operation.equals("/")){//edit
                                     totalNewBalance = clientOldBalance - incomeOldBalance + incomeNewBalance;
                                 }
                                 snapshot.getRef().child("balance").setValue(totalNewBalance);
                             }
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
    private void editClientIncome(String projectKey,String incomeKey,double newIncomeBalance){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Incomes").child(incomeKey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double incomeOldBalance = snapshot.getValue(IncomeModel.class).getBalance();
                   controlClientBalance(projectKey,newIncomeBalance,"/",incomeOldBalance);
                   controlClientBalanceInProject(projectKey,newIncomeBalance,"/",incomeOldBalance);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void editProjectIncome(String projectKey ,String incomeKey){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey).child("Incomes").child(incomeKey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   double balance1 = snapshot.getValue(IncomeModel.class).getBalance();
                   databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           double t = snapshot.getValue(ProjectModel.class).getIncome();
                           double newBalance = Double.parseDouble(binding.secondField.getText().toString());
                           snapshot.getRef().child("income").setValue(t-balance1+newBalance);
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
    private void getMainClientModel(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Projects").child(projectKey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists()){
                  ProjectModel projectModel = snapshot.getValue(ProjectModel.class);
                  binding.firstField.setText(projectModel.getName());
                  mainClientModel = projectModel.getClientModel();
                  mainClientModel.setKey(projectModel.getClientModel().getKey());
                  binding.secondField.setText(projectModel.getClientModel().getName());
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
           if (status.equals("addTrader")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Traders.class);
               startActivity(intent);
               finish();
           } else if (status.equals("addWorker")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Workers.class);
               startActivity(intent);
               finish();
           } else if (status.equals("addIncome")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Income.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }else if (status.equals("editIncome")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Income.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           } else if (status.equals("editTrader")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Traders.class);
               startActivity(intent);
               finish();
           } else if (status.equals("editWorker")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Workers.class);
               startActivity(intent);
               finish();
           } else if (status.equals("addClient")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Clients.class);
               startActivity(intent);
               finish();
           } else if (status.equals("editClient")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Clients.class);
               startActivity(intent);
               finish();
           } else if (status.equals("addProject")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Projects.class);
               startActivity(intent);
               finish();
           }else if (status.equals("editProject")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Projects.class);
               startActivity(intent);
               finish();
           }else if (status.equals("editWorkerDebts")) {
               Intent intent = new Intent(AddTraderOrWorker.this, Debts.class);
               intent.putExtra("projectKey",projectKey);
               startActivity(intent);
               finish();
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    private void setProjectView(){
        binding.projectName.setVisibility(View.GONE);
        binding.firstField.setHint(R.string.projectName);
        binding.secondField.setHint(R.string.clientName);
        binding.secondField.setFocusable(false);
        binding.secondField.setFocusableInTouchMode(false);
        binding.secondField.setClickable(false);
        binding.secondField.setCursorVisible(false);
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