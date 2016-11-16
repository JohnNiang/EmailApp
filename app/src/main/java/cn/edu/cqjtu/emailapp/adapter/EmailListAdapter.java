package cn.edu.cqjtu.emailapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import cn.edu.cqjtu.emailapp.R;

/**
 * Created by JohnNiang on 2016/11/14.
 */

public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.ViewHolder> {

    private final Map<Integer, String> msgContent;

    private OnClickCheckButtonListener listener;

    public EmailListAdapter(Map<Integer, String> msgContent,OnClickCheckButtonListener listener) {
        this.msgContent = msgContent;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (!msgContent.containsKey(position + 1)) {
            return;
        }
        final String value = msgContent.get(position + 1);
        holder.mTvNo.setText((position + 1)+"");
        holder.mTvSubject.setText(value.substring(0, 20));
        holder.btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/11/14 完成打开邮件的点击事件
                listener.startEmailContentAty(position+1,value);
            }
        });
    }

    @Override
    public int getItemCount() {
        return msgContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTvNo;
        public final TextView mTvSubject;
        public final Button btnCheckEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTvNo = (TextView) mView.findViewById(R.id.tvNo);
            mTvSubject = (TextView) mView.findViewById(R.id.tvSubject);
            btnCheckEmail = (Button) mView.findViewById(R.id.btnCheckEmail);
        }
    }

    public interface OnClickCheckButtonListener {
        void startEmailContentAty(int no, String content);
    }
}
