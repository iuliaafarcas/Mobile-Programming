import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:kiddoz_in_flutter/utils/Recommendation.dart';
import 'package:kiddoz_in_flutter/utils/Repo.dart';
import 'package:provider/provider.dart';

import 'UpdatePage.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final _biggerFont = const TextStyle(fontSize: 18);

  _showDialog(BuildContext context, Repo repo, int id) {
    showDialog(
        context: context,
        builder: (context) => CupertinoAlertDialog(
              title: const Text("CupertinoAlertDialog"),
              content: const Text("Are you sure you want to delete this item?"),
              actions: <Widget>[
                CupertinoDialogAction(
                  child: const Text("Yes"),
                  onPressed: () {
                    repo.removeFromList(id);
                    Navigator.of(context).pop();
                  },
                ),
                CupertinoDialogAction(
                  child: const Text("No"),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),
              ],
            ));
  }

  @override
  Widget build(BuildContext context) {
    final repo = Provider.of<Repo>(context);

    return Scaffold(
      body: NestedScrollView(
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return <Widget>[
              const CupertinoSliverNavigationBar(
                largeTitle: Text("Kiddoz"),
              )
            ];
          },
          body: SizedBox(
            height: 350,
            child: ListView.builder(
              itemCount: repo.getListSize() * 2,
              padding: const EdgeInsets.only(
                  left: 16.0, top: 16.0, right: 16.0, bottom: 50.0),
              itemBuilder: /*1*/ (context, i) {
                if (i.isOdd) return const Divider();
                /*2*/
                final index = i ~/ 2; /*3*/

                return Column(children: [
                  Text(
                    repo.getRecommendationList()[index].toStr(),
                    style: _biggerFont,
                  ),
                  Row(children: [
                    SizedBox(
                      width: 100,
                      child: CupertinoButton.filled(
                        padding: const EdgeInsetsDirectional.only(
                            start: 15, end: 15),
                        onPressed: () {
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (context) => UpdatePage(
                                  recommendation:
                                      repo.getRecommendationList()[index])));
                        },
                        child: const Text('Update'),
                      ),
                    ),
                    const SizedBox(
                      width: 10.0,
                    ),
                    SizedBox(
                        width: 100,
                        child: CupertinoButton.filled(
                          padding: const EdgeInsetsDirectional.only(
                              start: 15, end: 15),
                          onPressed: () {
                            _showDialog(context, repo,
                                repo.getRecommendationList()[index].id);
                          },
                          child: const Text('Delete'),
                        ))
                  ])
                ]);
              },
            ),
          )),
    );
  }
}
