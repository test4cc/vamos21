package desktopsearcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import specifications.Configuration;

public class SearchResultTree implements TreeSelectionListener {
	private JTree tree;
	private JPanel _hit_view;

	public SearchResultTree(TreeMap hits, JPanel hit_view) {
		if (Configuration.GUI) {
			if (Configuration.TREE_VIEW) {
				_hit_view = hit_view;

				// Create the nodes.
				DefaultMutableTreeNode top = new DefaultMutableTreeNode(
						"Results");
				createNodes(top, hits);

				// Create a tree that allows one selection at a time.
				tree = new JTree(top);
				tree.getSelectionModel().setSelectionMode(
						TreeSelectionModel.SINGLE_TREE_SELECTION);

				// Listen for when the selection changes.
				tree.addTreeSelectionListener(this);
			}
		}
	}

	public JTree getTree() {
		if (Configuration.GUI) {
			if (Configuration.TREE_VIEW) {
				return tree;
			}
		}
		return null;
	}

	public void createNodes(DefaultMutableTreeNode top, TreeMap hits) {
		if (Configuration.GUI) {
			if (Configuration.TREE_VIEW) {
				TreeMap path_map = buildPathMap(hits);
				iterateMap(top, "", path_map);
			}
		}
	}

	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {
		if (Configuration.GUI) {
			if (Configuration.TREE_VIEW) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();

				if (node == null)
					return;

				Object nodeInfo = node.getUserObject();
				if (node.isLeaf()) {
					HitDocument doc = (HitDocument) nodeInfo;
					// System.out.println(doc.getPath());
					_hit_view.removeAll();
					_hit_view.add(doc).setBounds(0, 0,
							_hit_view.getPreferredSize().width,
							_hit_view.getPreferredSize().height);
					_hit_view.repaint();
				} else {

				}
			}
		}
	}

	public void iterateMap(DefaultMutableTreeNode top, String path, TreeMap map) {
		if (Configuration.GUI) {
			if (Configuration.TREE_VIEW) {
				LinkedList dir_list = new LinkedList();
				LinkedList dir_key_list = new LinkedList();
				LinkedList file_list = new LinkedList();

				Set keys = map.keySet();
				Iterator it = keys.iterator();

				while (it.hasNext()) {
					String key = (String) it.next();
					Object o = map.get(key);

					if (o instanceof TreeMap) {
						dir_list.add(o);
						dir_key_list.add(key);
					} else if (o instanceof HitDocument) {
						file_list.add(o);
					}
				}

				// mehr als ein item oder genau eine datei
				if (keys.size() > 1
						|| (keys.size() == 1 && file_list.size() == 1)) {
					String top_path = (String) top.getUserObject();
					// System.out.println("Top node: " + top_path);
					// System.out.println("aktueller Path: " + path);
					if (!top_path.equals(path)) {
						DefaultMutableTreeNode root_node = new DefaultMutableTreeNode(
								path);
						top.add(root_node);
						top = root_node;
						// System.out.println("neuer Top node: " + (String)
						// top.getUserObject());
					}
				}
				// nur ein verzeichnis
				else if (dir_list.size() == 1 && keys.size() == 1) {
					String key = (String) dir_key_list.getFirst();
					iterateMap(top, path + key + OS.getPathSeparator(),
							(TreeMap) dir_list.getFirst());
					return;
				}
				// else if(file_list.size()==1 && keys.size() == 1)
				// {
				// String key = (String) dir_key_list.getFirst();
				// iterateMap(top, path + key + OS.getPathSeparator(), (TreeMap)
				// dir_list.getFirst());
				// return;
				// }

				Iterator dir_it = dir_list.iterator();
				Iterator key_it = dir_key_list.iterator();

				Iterator file_it = file_list.iterator();

				while (dir_it.hasNext() && key_it.hasNext()) {
					TreeMap dir_map = (TreeMap) dir_it.next();
					String key = (String) key_it.next();

					if (dir_list.size() == keys.size()) {
						iterateMap(top, key + OS.getPathSeparator(), dir_map);
					} else {
						DefaultMutableTreeNode node = new DefaultMutableTreeNode(
								key + OS.getPathSeparator());
						// System.out.println("new node for: " + key +
						// OS.getPathSeparator());
						top.add(node);
						iterateMap(node, key + OS.getPathSeparator(), dir_map);
					}
				}

				while (file_it.hasNext()) {
					HitDocument doc = (HitDocument) file_it.next();
					// System.out.println(doc.getPath());
					top.add(new DefaultMutableTreeNode(doc));
					// System.out.println("new node for: " + doc);
				}
			}
		}
	}

	public TreeMap buildPathMap(TreeMap hits) {
		TreeMap map = new TreeMap();

		Collection c = hits.values();
		Iterator it = c.iterator();

		while (it.hasNext()) {
			TreeMap current_map = map;
			TreeMap next = null;
			HitDocument hit = (HitDocument) it.next();
			String[] splitted_path = hit.getPath().split(OS.getPathSeparator());
			for (int i = 0; i < splitted_path.length; i++) {
				// System.out.println(i + ": " + splitted_path[i]);
				if (i == splitted_path.length - 1) {
					current_map.put(splitted_path[i], hit);
				} else {
					next = (TreeMap) current_map.get(splitted_path[i]);
					if (next == null) {
						// System.out.println("Pfad noch nicht vorhanden: " +
						// splitted_path[i]);
						next = new TreeMap();
						current_map.put(splitted_path[i], next);
					}
					current_map = next;
				}
			}
		}

		return map;
	}
}
