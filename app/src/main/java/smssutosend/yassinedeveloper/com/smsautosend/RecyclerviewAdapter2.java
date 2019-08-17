package smssutosend.yassinedeveloper.com.smsautosend;

/**
 * Created by root on 10/19/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by csa on 3/6/2017.
 */

public class RecyclerviewAdapter2 extends RecyclerView.Adapter<RecyclerviewAdapter2.MyHolder>{


    List<DataDetails>  dataDetails;


    public RecyclerviewAdapter2(List<DataDetails> dataDetails) {
        this.dataDetails = dataDetails;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_layout, parent, false);

    MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }
    public void onBindViewHolder(MyHolder holder, int position) {
        DataDetails data = dataDetails.get(position);
        try{
            String dd = data.getType();
            holder.layout_view.setBackgroundColor(Color.LTGRAY);
           // holder.layout_view.setVisibility(View.GONE);

        }catch (Exception e){

        }

        holder.type.setText("type :  "+data.getType());
        holder.number.setText("number :"+data.getNumber());
        holder.status.setText("date test : "+data.getstatus());
        holder.date.setText("date number :" +data.getDate());
    }

    @Override
    public int getItemCount() {
        return dataDetails.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView number, type, status, date;
        LinearLayout layout_view;
        private final Context context;

        public MyHolder(final View itemView) {

            super(itemView);

            context = itemView.getContext();
            final Intent[] intent = new Intent[1];
            layout_view = (LinearLayout) itemView.findViewById(R.id.layout_view);
            number = (TextView) itemView.findViewById(R.id.number);
            type = (TextView) itemView.findViewById(R.id.type);
            status = (TextView) itemView.findViewById(R.id.status);
            date = (TextView) itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*
                    String un = userDetails.get(getAdapterPosition()).getUserName();
                    String EMail = userDetails.get(getAdapterPosition()).getEmail();

                    intent[0] = new Intent(context, MainActivity.class);
                    intent[0].putExtra("friendname", un);
                    intent[0].putExtra("friendemail",EMail);
                    context.startActivity(intent[0]);
*/
                }
            });
        }
    }
}