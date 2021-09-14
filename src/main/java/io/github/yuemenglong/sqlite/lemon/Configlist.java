package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.lemon.common.IList;
import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.lemon.util.Assert;

@SuppressWarnings("UnnecessaryReturnStatement")
public class Configlist {
  public static Config freelist = null;      /* List of free configurations */
  public static Config current = null;       /* Top of list of configurations */
  public static Addr<Config> currentend = null;   /* Last on list of configs */
  public static Config basis = null;         /* Top of list of basis configs */
  public static Addr<Config> basisend = null;     /* End of list of basis configs */


  public static Config newConfig() {
    Config new_;
    if (freelist == null) {
      int i;
      int amt = 3;
      freelist = IList.malloc(amt, Config::new, (prev, next) -> prev.next = next);
    }
    new_ = freelist;
    freelist = freelist.next;
    return new_;
  }

  public static void deleteConfig(Config old) {
    old.next = freelist;
    freelist = old;
  }

  public static void init() {
    current = null;
    currentend = new Addr<>(() -> current, v -> current = v);
    basis = null;
    basisend = new Addr<>(() -> basis, v -> basis = v);
    Config.init();
  }

  public static void reset() {
    current = null;
    currentend = new Addr<>(() -> current, v -> current = v);
    basis = null;
    basisend = new Addr<>(() -> basis, v -> basis = v);
    Config.clear(null);
  }

  public static Config add(Rule rp, int dot) {
    Config cfp;
    Config model = new Config();
    Assert.assertTrue(currentend != null);
    model.rp = rp;
    model.dot = dot;
    cfp = Config.find(model);
    if (cfp == null) {
      cfp = newConfig();
      Config c = cfp;
      cfp.rp = rp;
      cfp.dot = dot;
      cfp.fws = Set.new_();
      cfp.stp = null;
      cfp.bplp = null;
      cfp.fplp = null;
      cfp.next = null;
      cfp.bp = null;
      currentend.set(cfp);
      currentend = new Addr<>(() -> c.next, v -> c.next = v);
      Config.insert(cfp);
    }
    return cfp;
  }

  public static Config addbasis(Rule rp, int dot) {
    Config cfp;
    Config model = new Config();
    Assert.assertTrue(basisend != null);
    Assert.assertTrue(currentend != null);
    model.rp = rp;
    model.dot = dot;
    cfp = Config.find(model);
    if (cfp == null) {
      cfp = newConfig();
      Config c = cfp;
      cfp.rp = rp;
      cfp.dot = dot;
      cfp.fws = Set.new_();
      cfp.stp = null;
      cfp.bplp = null;
      cfp.fplp = null;
      cfp.next = null;
      cfp.bp = null;
      currentend.set(cfp);
      currentend = new Addr<>(() -> c.next, v -> c.next = v);
      basisend.set(cfp);
      basisend = new Addr<>(() -> c.bp, v -> c.bp = v);
      Config.insert(cfp);
    }
    return cfp;
  }

  public static void closure(Lemon lemp) {
    Config cfp, newcfp;
    Rule rp, newrp;
    Symbol sp, xsp;
    int i, dot;
    Assert.assertTrue(currentend != null);
    for (cfp = current; cfp != null; cfp = cfp.next) {
      rp = cfp.rp;
      dot = cfp.dot;
      if (dot >= rp.nrhs) continue;
      sp = rp.rhs[dot];
      if (sp.type == Symbol.SymbolType.NONTERMINAL) {
        if (sp.rule == null && sp != lemp.errsym) {
          Error.msg(lemp.filename, rp.line, String.format("Nonterminal \"%s\" has no rules.",
                  sp.name));
          lemp.errorcnt++;
        }
        for (newrp = sp.rule; newrp != null; newrp = newrp.nextlhs) {
          newcfp = Configlist.add(newrp, 0);
          for (i = dot + 1; i < rp.nrhs; i++) {
            xsp = rp.rhs[i];
            if (xsp.type == Symbol.SymbolType.TERMINAL) {
              Set.add(newcfp.fws, xsp.index);
              break;
            } else {
              Set.union(newcfp.fws, xsp.firstset);
              if (!xsp.lambda) break;
            }
          }
          if (i == rp.nrhs) {
            Config c = cfp;
            Plink.add(new Addr<>(() -> c.fplp, v -> c.fplp = v), newcfp);
          }
        }
      }
    }
    return;
  }

  public static void sort() {
    current = Msort.msort(current,
            prev -> prev.next,
            (prev, next) -> prev.next = next,
            Config::cmp);
    currentend = null;
  }

  //
  ///* Sort the configuration list */
  //void Configlist_sort(){
  //  current = (struct config *)msort(current,&(current->next),Configcmp);
  //  currentend = 0;
  //  return;
  //}
  //
  public static void sortbasis() {
    basis = Msort.msort(current,
            prev -> prev.bp,
            (prev, next) -> prev.bp = next,
            Config::cmp);
    basisend = null;
  }

  ///* Sort the basis configuration list */
  //void Configlist_sortbasis(){
  //  basis = (struct config *)msort(current,&(current->bp),Configcmp);
  //  basisend = 0;
  //  return;
  //}
  //
  ///* Return a pointer to the head of the configuration list and
  //** reset the list */
  public static Config return_() {
    Config old = current;
    current = null;
    currentend = null;
    return old;
  }

  //struct config *Configlist_return(){
  //  struct config *old;
  //  old = current;
  //  current = 0;
  //  currentend = 0;
  //  return old;
  //}
  //
  public static Config basis() {
    Config old = basis;
    basis = null;
    basisend = null;
    return old;
  }

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
  public static void eat(Config cfp) {
    Config nextcfp;
    for (; cfp != null; cfp = nextcfp) {
      nextcfp = cfp.next;
      Assert.assertTrue(cfp.fplp == null);
      Assert.assertTrue(cfp.bplp == null);
      if (cfp.fws != null) Set.free(cfp.fws);
      deleteConfig(cfp);
    }
  }
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
