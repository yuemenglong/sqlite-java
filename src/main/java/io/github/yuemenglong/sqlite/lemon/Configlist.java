package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INext;

import java.util.function.Supplier;

public class Configlist {
    private static Config freelist = null;      /* List of free configurations */
    private static Config current = null;       /* Top of list of configurations */
    private static Config[] currentend = null;   /* Last on list of configs */
    private static Config basis = null;         /* Top of list of basis configs */
    private static Config[] basisend = null;     /* End of list of basis configs */

    public static Config newConfig() {
        Config new_;
        if (freelist == null) {
            int i;
            int amt = 3;
            freelist = INext.malloc(amt, Config::new);
        }
        new_ = freelist;
        freelist = freelist.next;
        return new_;
    }
    //
    ///* Return a pointer to a new configuration */
    //PRIVATE struct config *newconfig(){
    //  struct config *new;
    //  if( freelist==0 ){
    //    int i;
    //    int amt = 3;
    //    freelist = (struct config *)malloc( sizeof(struct config)*amt );
    //    if( freelist==0 ){
    //      fprintf(stderr,"Unable to allocate memory for a new configuration.");
    //      exit(1);
    //    }
    //    for(i=0; i<amt-1; i++) freelist[i].next = &freelist[i+1];
    //    freelist[amt-1].next = 0;
    //  }
    //  new = freelist;
    //  freelist = freelist->next;
    //  return new;
    //}
    //
    ///* The configuration "old" is no longer used */
    //PRIVATE void deleteconfig(old)
    //struct config *old;
    //{
    //  old->next = freelist;
    //  freelist = old;
    //}
    //
    ///* Initialized the configuration list builder */
    //void Configlist_init(){
    //  current = 0;
    //  currentend = &current;
    //  basis = 0;
    //  basisend = &basis;
    //  Configtable_init();
    //  return;
    //}
    //
    ///* Initialized the configuration list builder */
    //void Configlist_reset(){
    //  current = 0;
    //  currentend = &current;
    //  basis = 0;
    //  basisend = &basis;
    //  Configtable_clear(0);
    //  return;
    //}
    //
    ///* Add another configuration to the configuration list */
    //struct config *Configlist_add(rp,dot)
    //struct rule *rp;    /* The rule */
    //int dot;            /* Index into the RHS of the rule where the dot goes */
    //{
    //  struct config *cfp, model;
    //
    //  assert( currentend!=0 );
    //  model.rp = rp;
    //  model.dot = dot;
    //  cfp = Configtable_find(&model);
    //  if( cfp==0 ){
    //    cfp = newconfig();
    //    cfp->rp = rp;
    //    cfp->dot = dot;
    //    cfp->fws = SetNew();
    //    cfp->stp = 0;
    //    cfp->fplp = cfp->bplp = 0;
    //    cfp->next = 0;
    //    cfp->bp = 0;
    //    *currentend = cfp;
    //    currentend = &cfp->next;
    //    Configtable_insert(cfp);
    //  }
    //  return cfp;
    //}
    //
    ///* Add a basis configuration to the configuration list */
    //struct config *Configlist_addbasis(rp,dot)
    //struct rule *rp;
    //int dot;
    //{
    //  struct config *cfp, model;
    //
    //  assert( basisend!=0 );
    //  assert( currentend!=0 );
    //  model.rp = rp;
    //  model.dot = dot;
    //  cfp = Configtable_find(&model);
    //  if( cfp==0 ){
    //    cfp = newconfig();
    //    cfp->rp = rp;
    //    cfp->dot = dot;
    //    cfp->fws = SetNew();
    //    cfp->stp = 0;
    //    cfp->fplp = cfp->bplp = 0;
    //    cfp->next = 0;
    //    cfp->bp = 0;
    //    *currentend = cfp;
    //    currentend = &cfp->next;
    //    *basisend = cfp;
    //    basisend = &cfp->bp;
    //    Configtable_insert(cfp);
    //  }
    //  return cfp;
    //}
    //
    ///* Compute the closure of the configuration list */
    //void Configlist_closure(lemp)
    //struct lemon *lemp;
    //{
    //  struct config *cfp, *newcfp;
    //  struct rule *rp, *newrp;
    //  struct symbol *sp, *xsp;
    //  int i, dot;
    //
    //  assert( currentend!=0 );
    //  for(cfp=current; cfp; cfp=cfp->next){
    //    rp = cfp->rp;
    //    dot = cfp->dot;
    //    if( dot>=rp->nrhs ) continue;
    //    sp = rp->rhs[dot];
    //    if( sp->type==NONTERMINAL ){
    //      if( sp->rule==0 && sp!=lemp->errsym ){
    //        ErrorMsg(lemp->filename,rp->line,"Nonterminal \"%s\" has no rules.",
    //          sp->name);
    //        lemp->errorcnt++;
    //      }
    //      for(newrp=sp->rule; newrp; newrp=newrp->nextlhs){
    //        newcfp = Configlist_add(newrp,0);
    //        for(i=dot+1; i<rp->nrhs; i++){
    //          xsp = rp->rhs[i];
    //          if( xsp->type==TERMINAL ){
    //            SetAdd(newcfp->fws,xsp->index);
    //            break;
    //	  }else{
    //            SetUnion(newcfp->fws,xsp->firstset);
    //            if( xsp->lambda==FALSE ) break;
    //	  }
    //	}
    //        if( i==rp->nrhs ) Plink_add(&cfp->fplp,newcfp);
    //      }
    //    }
    //  }
    //  return;
    //}
    //
    ///* Sort the configuration list */
    //void Configlist_sort(){
    //  current = (struct config *)msort(current,&(current->next),Configcmp);
    //  currentend = 0;
    //  return;
    //}
    //
    ///* Sort the basis configuration list */
    //void Configlist_sortbasis(){
    //  basis = (struct config *)msort(current,&(current->bp),Configcmp);
    //  basisend = 0;
    //  return;
    //}
    //
    ///* Return a pointer to the head of the configuration list and
    //** reset the list */
    //struct config *Configlist_return(){
    //  struct config *old;
    //  old = current;
    //  current = 0;
    //  currentend = 0;
    //  return old;
    //}
    //
    ///* Return a pointer to the head of the configuration list and
    //** reset the list */
    //struct config *Configlist_basis(){
    //  struct config *old;
    //  old = basis;
    //  basis = 0;
    //  basisend = 0;
    //  return old;
    //}
    //
    ///* Free all elements of the given configuration list */
    //void Configlist_eat(cfp)
    //struct config *cfp;
    //{
    //  struct config *nextcfp;
    //  for(; cfp; cfp=nextcfp){
    //    nextcfp = cfp->next;
    //    assert( cfp->fplp==0 );
    //    assert( cfp->bplp==0 );
    //    if( cfp->fws ) SetFree(cfp->fws);
    //    deleteconfig(cfp);
    //  }
    //  return;
    //}
}
