package com.genius.groupie;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;
import java.util.Map;

/**
 * The base unit of content for a GroupAdapter.
 * <p>
 * Because an Item is a Group of size one, you don't need to use Groups directly if you don't want;
 * simply mix and match Items and add directly to the adapter.
 * <p>
 * If you want to use Groups, because Item extends Group, you can mix and match adding Items and
 * other Groups directly to the adapter.
 *
 * @param <T>
 */
public abstract class Item<T extends ViewDataBinding> implements Group, SpanSizeProvider {

    protected GroupDataObserver parentDataObserver;

    public void bind(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads, View.OnClickListener onItemClickListener) {
        ViewHolder<T> holder = (ViewHolder<T>) viewHolder;
        if (getExtras() != null) {
            holder.getExtras().putAll(getExtras());
        }
        holder.setDragDirs(getDragDirs());
        holder.setSwipeDirs(getSwipeDirs());
        T binding = holder.binding;
        boolean hasClickListener = isClickable() && onItemClickListener != null;
        binding.getRoot().setOnClickListener(hasClickListener ? onItemClickListener : null);
        bind(binding, position, payloads);
        binding.executePendingBindings();
    }

    @Override public int getSpanSize(int spanCount, int position) {
        return spanCount;
    }

    public int getSwipeDirs() {
        return 0;
    }

    public int getDragDirs() {
        return 0;
    }

    public abstract @LayoutRes int getLayout();

    public abstract void bind(T viewBinding, int position);

    /**
     * If you don't specify how to handle payloads in your implementation, they'll be ignored and
     * the adapter will do a full rebind.
     *
     * @param viewBinding
     * @param position
     * @param payloads
     */
    public void bind(T viewBinding, int position, List<Object> payloads) {
        bind(viewBinding, position);
    }

    @Override public int getItemCount() {
        return 1;
    }

    @Override public Item getItem(int position) {
        return this;
    }

    @Override public void setGroupDataObserver(GroupDataObserver groupDataObserver) {
        this.parentDataObserver = groupDataObserver;
    }

    @Override public int getPosition(Item item) {
        return this == item ? 0 : -1;
    }

    public boolean isClickable() {
        return true;
    }

    /**
     * A set of key/value pairs stored on the ViewHolder that can be useful for distinguishing
     * items of the same view type.
     *
     * @return
     */
    public Map<String, Object> getExtras() {
        return null;
    }
}
