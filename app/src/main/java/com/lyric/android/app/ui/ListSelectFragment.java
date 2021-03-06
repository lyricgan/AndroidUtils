package com.lyric.android.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseRecyclerAdapter;
import com.lyric.android.app.common.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * 列表选择Fragment，继承于{@link BottomSheetDialogFragment}
 * @author Lyric Gan
 * @since 2016/11/14 14:31
 */
public class ListSelectFragment extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private SelectAdapter mAdapter;
    private OnItemSelectListener mOnItemSelectListener;

    public static ListSelectFragment newInstance(List<ListSelectEntity> dataList) {
        ListSelectFragment fragment = new ListSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRAS_DATA, (Serializable) dataList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.bottom_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        List<ListSelectEntity> dataList = bundle.getParcelable(Constants.EXTRAS_DATA);
        mAdapter = new SelectAdapter(getContext());
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<ListSelectEntity>() {
            @Override
            public void onItemClick(int position, ListSelectEntity object, View itemView) {
                if (mOnItemSelectListener == null) {
                    return;
                }
                mOnItemSelectListener.onItemSelect(position, object, itemView);
            }
        });
        mAdapter.setDataList(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    public BaseRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    public void show(FragmentManager manager) {
        show(manager, "list_select_tag");
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.mOnItemSelectListener = listener;
    }

    class SelectAdapter extends BaseRecyclerAdapter<ListSelectEntity> {

        SelectAdapter(Context context) {
            super(context, R.layout.item_select_list);
        }

        @Override
        public void convert(View itemView, int position, ListSelectEntity item) {
            TextView tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            tvItemTitle.setText(item.getTitle());
        }
    }

    public interface OnItemSelectListener {

        void onItemSelect(int position, ListSelectEntity object, View itemView);
    }

    public static class ListSelectEntity implements Parcelable {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.title);
        }

        public ListSelectEntity() {
        }

        protected ListSelectEntity(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
        }

        public static final Parcelable.Creator<ListSelectEntity> CREATOR = new Parcelable.Creator<ListSelectEntity>() {
            @Override
            public ListSelectEntity createFromParcel(Parcel source) {
                return new ListSelectEntity(source);
            }

            @Override
            public ListSelectEntity[] newArray(int size) {
                return new ListSelectEntity[size];
            }
        };
    }
}