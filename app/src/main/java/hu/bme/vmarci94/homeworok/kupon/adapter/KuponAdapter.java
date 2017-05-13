package hu.bme.vmarci94.homeworok.kupon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import hu.bme.vmarci94.homeworok.kupon.R;
import hu.bme.vmarci94.homeworok.kupon.data.Kupon;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnKuponClickListener;

/**
 * Created by vmarci94 on 2017.05.05..
 */

public class KuponAdapter extends RecyclerView.Adapter<KuponAdapter.ViewHolder> {

    private Context context;
    private ArrayMap<String, Kupon> kuponArrayMap;

    private OnKuponClickListener listener;
    //private int lastPosition = -1;

    public KuponAdapter(Context context, OnKuponClickListener listener) {
        this.context = context;
        this.kuponArrayMap = new ArrayMap<>();
        this.listener = listener;
    }

    public final Kupon get(String key) {
        Kupon tmp = this.kuponArrayMap.get(key);
        if(tmp != null){
            return tmp;
        }else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mItemView;
        public TextView tvCompany;
        public TextView tvSale;
        public TextView tvDescription;
        public ImageView imgKuponKod;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            tvCompany = (TextView) itemView.findViewById(R.id.tvCompany);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvSale = (TextView) itemView.findViewById(R.id.tvSale);
            imgKuponKod = (ImageView) itemView.findViewById(R.id.imgKuponKod); //Átgondolni
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_kupons, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Kupon tmpKupon = kuponArrayMap.valueAt(viewHolder.getAdapterPosition()); //FIXME ?

        viewHolder.tvCompany.setText(tmpKupon.getCompany());
        viewHolder.tvDescription.setText(tmpKupon.getDescription());
        viewHolder.tvSale.setText(tmpKupon.getSale());
        viewHolder.imgKuponKod.setImageResource(R.drawable.error);
        tmpKupon.setImgKupon(viewHolder.imgKuponKod);

        //viewHolder.imgKuponKod.setVisibility(View.INVISIBLE); // FIXME et itt még át kell gondolni
        viewHolder.imgKuponKod.setVisibility(View.VISIBLE);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //itt jön a nagyobb kép.
                if(listener != null){
                    //Itt már eléred a position-t is

                    listener.onKuponClicked( "dummy");
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                listener.onKuponLongClick();
                return true;
            }
        });


        setAnimation(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return kuponArrayMap.size();
    }

    public void addKupon(String key, Kupon kupon) {
        kuponArrayMap.put(key ,kupon);
        notifyDataSetChanged();
    }

    public void removeKupon(String key){
        kuponArrayMap.remove(key);
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
//        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);

//            lastPosition = position;}
    }

    public ArrayList<Double[]> getAllKupon(){
        ArrayList<Double[]> posLatLong = new ArrayList<>();
        for(int i = 0; i < kuponArrayMap.size(); i++){
            posLatLong.add(new Double[]{kuponArrayMap.valueAt(i).getLatitude(), kuponArrayMap.valueAt(i).getLongitude()});
        }
        return posLatLong;
    }

    public void update(String key){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ key + ".png");
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into( kuponArrayMap.get(key).getImgKupon() );
        notifyDataSetChanged();
    }


}
