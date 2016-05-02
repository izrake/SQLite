package db.prasant.com.sqlitedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
     EditText  editRoll;
     EditText editName;
     EditText editMarks;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editRoll=(EditText)findViewById(R.id.editRoll);
        editName=(EditText)findViewById(R.id.editName);
       editMarks =(EditText)findViewById(R.id.editMarks);
       Button btnAdd=(Button)findViewById(R.id.btnAdd);
        Button btnDelete=(Button)findViewById(R.id.btnDelete);
        Button btnModify=(Button)findViewById(R.id.btnModify);
        Button btnView=(Button)findViewById(R.id.btnView);
        Button btnViewAll=(Button)findViewById(R.id.btnViewAll);
        Button btnShowInfo=(Button)findViewById(R.id.btnShow);
        Button btninsert=(Button)findViewById(R.id.insert);

        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRoll.getText().toString().trim().length() == 0 ||
                        editName.getText().toString().trim().length() == 0 ||
                        editMarks.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter all values");
                    return;
                }
                // Inserting record
                db.execSQL("INSERT INTO student VALUES('" + editRoll.getText() + "','" + editName.getText() +
                        "','" + editMarks.getText() + "');");
                showMessage("Success", "Record added");
                clearText();
            }

        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRoll.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                // Searching roll number
                Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRoll.getText()+"'", null);
                if(c.moveToFirst())
                {
                    // Deleting record if found
                    db.execSQL("DELETE FROM student WHERE rollno='"+editRoll.getText()+"'");
                    showMessage("Success", "Record Deleted");
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                }
                clearText();
            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRoll.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                // Searching roll number
                Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRoll.getText()+"'", null);
                if(c.moveToFirst())
                {
                    // Modifying record if found
                    db.execSQL("UPDATE student SET name='"+editName.getText()+"',marks='"+editMarks.getText()+
                            "' WHERE rollno='"+editRoll.getText()+"'");
                    showMessage("Success", "Record Modified");
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                }
                clearText();
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRoll.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                // Searching roll number
                Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRoll.getText()+"'", null);
                if(c.moveToFirst())
                {
                    // Displaying record if found
                    editName.setText(c.getString(1));
                    editMarks.setText(c.getString(2));
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                    clearText();
                }

            }
        });
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c=db.rawQuery("SELECT * FROM student", null);
                // Checking if no records found
                if(c.getCount()==0)
                {
                    showMessage("Error", "No records found");
                    return;
                }
                // Appending records to a string buffer
                StringBuffer buffer=new StringBuffer();
                while(c.moveToNext())
                {
                    buffer.append("Rollno: "+c.getString(0)+"\n");
                    buffer.append("Name: "+c.getString(1)+"\n");
                    buffer.append("Marks: "+c.getString(2)+"\n\n");
                }
                // Displaying all records
                showMessage("Student Details", buffer.toString());
            }

        });
        btnShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Student Management Application", "Developed By Azim");
            }
        });

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("DROP student",null);
                showMessage("dropped","Table dropped");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        editRoll.setText("");
        editName.setText("");
        editMarks.setText("");
        editRoll.requestFocus();
    }
}
