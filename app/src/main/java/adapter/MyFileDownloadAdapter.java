package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.excelergo.niceexp.R;

import java.io.InputStream;
import java.util.List;

import interfaces.GoBackAction;
import utils.FileDownloadutil;
import utils.ImgeClipUtil;

public class MyFileDownloadAdapter extends RecyclerView.Adapter<MyFileDownloadAdapter.MyViewHolder> {
    private List<String> fileNames;
    private OnFileSelectedCallBack fileSelectedCallBack;
    private OnFileManageCallBack fileManageCallBack;
    private GoBackAction goBackAction;
    Context context;

    public MyFileDownloadAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyFileDownloadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_download_manage_item,viewGroup,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
                myViewHolder.textView.setText(fileNames.get(i));
                String endName=myViewHolder.textView.getText().toString();
                switch (endName.substring(endName.indexOf(".")+1)){
                    case "mp3":
                        InputStream is=context.getResources().openRawResource(R.raw.mp3);
                        Bitmap bitmap= BitmapFactory.decodeStream(is);
                        myViewHolder.imageView.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap,200,bitmap.getWidth()));
                    break;
                    case "mp4":
                        InputStream is2=context.getResources().openRawResource(R.raw.mp4);
                        Bitmap bitmap2= BitmapFactory.decodeStream(is2);
                        myViewHolder.imageView.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap2,200,bitmap2.getWidth()));
                    break;
                    case "apk": InputStream is3=context.getResources().openRawResource(R.raw.apk);
                        Bitmap bitmap3= BitmapFactory.decodeStream(is3);
                        myViewHolder.imageView.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap3,200,bitmap3.getWidth()));
                    break;
                    case "png":
                    case "jpg":
                    case "jpeg":InputStream is4=context.getResources().openRawResource(R.raw.picture);
                        Bitmap bitmap4= BitmapFactory.decodeStream(is4);
                        myViewHolder.imageView.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap4,200,bitmap4.getWidth()));
                        break;
                    default:  InputStream is5=context.getResources().openRawResource(R.raw.file);
                        Bitmap bitmap5= BitmapFactory.decodeStream(is5);
                        myViewHolder.imageView.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap5,200,bitmap5.getWidth()));
                    break;
                }
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fileSelectedCallBack!=null){
                            fileSelectedCallBack.onOpenFile(fileNames.get(i));
                        }
                    }
                });
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu menu=new PopupMenu(context,view);
                        menu.getMenuInflater().inflate(R.menu.menu,menu.getMenu());
                        menu.show();
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.check:
                                        if(fileSelectedCallBack!=null){
                                            fileSelectedCallBack.onOpenFile(fileNames.get(i));
                                        }
                                        break;
                                    case R.id.delete:
                                        FileDownloadutil.deleteSpecificFile(fileNames.get(i));
                                        if(goBackAction!=null){
                                            goBackAction.goBack(33);
                                        }

                                        break;
                                    case R.id.open:
                                        if(fileManageCallBack!=null){
                                            fileManageCallBack.onOpenFilelocation(fileNames.get(i));
                                        }
                                        break;
                                }
                                return false;
                            }
                        });
                        return false;
                    }
                });
    }


    @Override
    public int getItemCount() {
        fileNames=FileDownloadutil.listAllFileNmae();
        return fileNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.fileName);
            imageView=itemView.findViewById(R.id.fileIcon);
        }
    }

    public void setFileSelectedCallBack(OnFileSelectedCallBack fileSelectedCallBack) {
        this.fileSelectedCallBack = fileSelectedCallBack;
    }

    public void setFileManageCallBack(OnFileManageCallBack fileManageCallBack) {
        this.fileManageCallBack = fileManageCallBack;
    }

    public void setGoBackAction(GoBackAction goBackAction) {
        this.goBackAction = goBackAction;
    }
    public interface OnFileManageCallBack {
        void onOpenFilelocation(String fileLocation);
    }
    public interface OnFileSelectedCallBack {
        void onOpenFile(String fileName);
    }
}
