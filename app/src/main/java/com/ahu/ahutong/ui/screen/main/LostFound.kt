package com.ahu.ahutong.ui.screen.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ahu.ahutong.data.crawler.model.adwnh.LostFoundItem
import com.ahu.ahutong.ui.shape.SmoothRoundedCornerShape
import com.ahu.ahutong.ui.state.LostFoundViewModel
import com.kyant.capsule.ContinuousCapsule
import com.kyant.monet.a1
import com.kyant.monet.n1
import com.kyant.monet.withNight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostFound(
    lostFoundViewModel: LostFoundViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val allCampus = lostFoundViewModel.allCampus?.`object`.orEmpty()
    val allLostFoundType =
        lostFoundViewModel.allLostFoundType?.`object`.orEmpty()
    val lostFoundList =
        lostFoundViewModel.lostFoundList?.data?.list.orEmpty()

    var searchExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var selectedCampus by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var selectedType by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var selectedItem by remember {
        mutableStateOf<LostFoundItem?>(null)
    }

    LaunchedEffect(Unit) {
        if (allCampus.isEmpty()) {
            lostFoundViewModel.getAllCampus()
        }

        if (allLostFoundType.isEmpty()) {
            lostFoundViewModel.getAllLostFoundType()
        }

        if (lostFoundList.isEmpty()) {
            lostFoundViewModel.getLostFoundList()
        }
    }

    val filteredList = lostFoundList.filter {
        val campusMatch =
            selectedCampus == null || it.campusid == selectedCampus

        val typeMatch =
            selectedType == null || it.typeid == selectedType

        val searchMatch =
            searchQuery.isBlank() ||
                    it.title.contains(searchQuery, true)

        campusMatch && typeMatch && searchMatch
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text = "失物招领",
                        style = MaterialTheme.typography
                            .headlineMedium
                    )

                    Row(
                        modifier = Modifier
                            .clip(ContinuousCapsule)
                            .background(
                                100.n1 withNight 30.n1
                            )
                    ) {
                        IconButton(
                            onClick = {
                                lostFoundViewModel
                                    .getLostFoundList()

                                Toast.makeText(
                                    context,
                                    "刷新成功",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default.Refresh,
                                contentDescription = null
                            )
                        }

                        IconButton(
                            onClick = {
                                searchExpanded =
                                    !searchExpanded

                                if (!searchExpanded) {
                                    searchQuery = ""
                                }
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (searchExpanded)
                                        Icons.Default.Close
                                    else
                                        Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    }
                }

                if (searchExpanded) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = ContinuousCapsule,
                        placeholder = {
                            Text("搜索物品")
                        }
                    )
                }
            }

            if (!searchExpanded) {

                // 校区筛选
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(ContinuousCapsule)
                        .background(
                            100.n1 withNight 20.n1
                        )
                        .padding(8.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = selectedCampus == null,
                        onClick = {
                            selectedCampus = null
                        },
                        label = {
                            Text("全部校区")
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        items(allCampus) { campus ->
                            val selected =
                                selectedCampus ==
                                        campus.id

                            Text(
                                text =
                                    campus.campusName,
                                modifier = Modifier
                                    .clip(
                                        ContinuousCapsule
                                    )
                                    .background(
                                        if (selected)
                                            90.a1
                                        else
                                            Color.Unspecified
                                    )
                                    .clickable {
                                        selectedCampus =
                                            campus.id
                                    }
                                    .padding(
                                        16.dp,
                                        8.dp
                                    ),
                                color =
                                    if (selected)
                                        0.n1
                                    else
                                        Color.Unspecified
                            )
                        }
                    }
                }

                // 类型筛选
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(ContinuousCapsule)
                        .background(
                            100.n1 withNight 20.n1
                        )
                        .padding(8.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = selectedType == null,
                        onClick = {
                            selectedType = null
                        },
                        label = {
                            Text("全部类型")
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        items(allLostFoundType) { type ->
                            val selected =
                                selectedType ==
                                        type.typeId

                            Text(
                                text = type.typeName,
                                modifier = Modifier
                                    .clip(
                                        ContinuousCapsule
                                    )
                                    .background(
                                        if (selected)
                                            90.a1
                                        else
                                            Color.Unspecified
                                    )
                                    .clickable {
                                        selectedType =
                                            type.typeId
                                    }
                                    .padding(
                                        16.dp,
                                        8.dp
                                    ),
                                color =
                                    if (selected)
                                        0.n1
                                    else
                                        Color.Unspecified
                            )
                        }
                    }
                }
            }

            Text(
                text =
                    "共 ${filteredList.size} 条记录",
                modifier =
                    Modifier.padding(horizontal = 24.dp),
                style =
                    MaterialTheme.typography.titleMedium
            )

            if (filteredList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(
                            SmoothRoundedCornerShape(
                                32.dp
                            )
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(2.dp)
                ) {
                    filteredList.forEach { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    SmoothRoundedCornerShape(
                                        4.dp
                                    )
                                )
                                .background(
                                    100.n1 withNight 20.n1
                                )
                                .clickable {
                                    selectedItem = item
                                }
                                .padding(
                                    24.dp,
                                    16.dp
                                ),
                            verticalArrangement =
                                Arrangement.spacedBy(
                                    8.dp
                                )
                        ) {
                            Text(
                                text = item.title,
                                fontWeight =
                                    FontWeight.Bold,
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium
                            )

                            Text(
                                text =
                                    "联系人：${item.linkman ?: "未知"}"
                            )

                            Text(
                                text =
                                    "联系电话：${item.phone ?: "未知"}"
                            )

                            Text(
                                text =
                                    "校区：${item.campusName ?: "未知"}"
                            )

                            Text(
                                text =
                                    "类型：${item.lostType?.typeName ?: "未知"}"
                            )

                            Text(
                                text =
                                    "证件号：${item.pubuser?.idNumber ?: "无"}"
                            )

                            Text(
                                text =
                                    "发布人：${item.pubuser?.userName ?: "未知"}"
                            )

                            Text(
                                text =
                                    item.createtime
                                        ?: "未知时间",
                                color =
                                    50.n1 withNight 80.n1
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "暂无数据",
                    modifier =
                        Modifier.padding(24.dp),
                    style =
                        MaterialTheme.typography
                            .titleLarge
                )
            }
        }

        // 详情弹窗
        selectedItem?.let { item ->
            ModalBottomSheet(
                onDismissRequest = {
                    selectedItem = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = item.title,
                        style =
                            MaterialTheme.typography
                                .headlineSmall,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        "联系人：${item.linkman ?: "未知"}"
                    )

                    Text(
                        "联系电话：${item.phone ?: "未知"}"
                    )

                    Text(
                        "校区：${item.campusName ?: "未知"}"
                    )

                    Text(
                        "类型：${item.lostType?.typeName ?: "未知"}"
                    )

                    Text(
                        "证件号：${item.pubuser?.idNumber ?: "无"}"
                    )

                    Text(
                        "发布人：${item.pubuser?.userName ?: "未知"}"
                    )

                    Text(
                        "发布时间：${item.createtime ?: "未知"}"
                    )

                    if (item.imgs.isNotEmpty()) {
                        Text(
                            text = "相关图片",
                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium
                        )

                        LazyRow(
                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    12.dp
                                )
                        ) {
                            items(item.imgs) { img ->
                                Card(
                                    modifier =
                                        Modifier.size(
                                            180.dp
                                        )
                                ) {
                                    AsyncImage(
                                        model =
                                            img.imgPath,
                                        contentDescription =
                                            null,
                                        modifier =
                                            Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}