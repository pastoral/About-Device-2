package aboutdevice.com.munir.symphony.mysymphony.utils;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by munirul.hoque on 11/16/2016.
 */

public class CCAddressViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    public TextView txtCCName;
    public TextView txtCCAddress;
    View mView;
    public CCAddressViewHolder(View mView){
        super(mView);
        this.mView = mView;
        txtCCName = (TextView)mView.findViewById(R.id.txtCCName);
        txtCCAddress = (TextView)mView.findViewById(R.id.txtCCAddress);
        mView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.add(0,view.getId(),0,"Delete");
    }
}
