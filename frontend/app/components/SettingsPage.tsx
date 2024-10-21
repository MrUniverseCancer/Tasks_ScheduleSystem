import React, {useState} from 'react';

interface SettingsPageProps {
    onCancel: () => void;
    onSave: () => void;
    onSettingsChange: () => void;
}

const SettingsPage: React.FC<SettingsPageProps> = ({onCancel, onSave, onSettingsChange}) => {
    const [syncTarget, setSyncTarget] = useState('WebDAV');
    const [webdavUrl, setWebdavUrl] = useState('');
    const [webdavUsername, setWebdavUsername] = useState('');
    const [webdavPassword, setWebdavPassword] = useState('');
    const [syncInterval, setSyncInterval] = useState('5');

    const handleChange = () => {
        onSettingsChange();
    };

    return (
        <div className="p-4">
            <h2 className="text-2xl font-semibold mb-4">同步目标</h2>

            <div className="mb-4">
                <select
                    value={syncTarget}
                    onChange={(e) => {
                        setSyncTarget(e.target.value);
                        handleChange();
                    }}
                    className="w-full p-2 border rounded"
                >
                    <option value="WebDAV">WebDAV</option>
                </select>
            </div>

            <div className="mb-4">
                <label className="block mb-2">WebDAV URL</label>
                <input
                    type="text"
                    value={webdavUrl}
                    onChange={(e) => {
                        setWebdavUrl(e.target.value);
                        handleChange();
                    }}
                    className="w-full p-2 border rounded"
                />
            </div>

            <div className="mb-4">
                <p className="text-sm text-gray-600">
                    注意：如果要更改该位置，请确保在同步之前所有内容都复制到该位置，否则将丢失所有文件！
                </p>
            </div>

            <div className="mb-4">
                <label className="block mb-2">WebDAV 用户名</label>
                <input
                    type="text"
                    value={webdavUsername}
                    onChange={(e) => {
                        setWebdavUsername(e.target.value);
                        handleChange();
                    }}
                    className="w-full p-2 border rounded"
                />
            </div>

            <div className="mb-4">
                <label className="block mb-2">WebDAV 密码</label>
                <input
                    type="password"
                    value={webdavPassword}
                    onChange={(e) => {
                        setWebdavPassword(e.target.value);
                        handleChange();
                    }}
                    className="w-full p-2 border rounded"
                />
            </div>

            <div className="mb-4">
                <label className="block mb-2">同步间隔</label>
                <select
                    value={syncInterval}
                    onChange={(e) => {
                        setSyncInterval(e.target.value);
                        handleChange();
                    }}
                    className="w-full p-2 border rounded"
                >
                    <option value="5">5 分钟</option>
                    {/* Add other interval options here */}
                </select>
            </div>

            <div className="flex justify-end space-x-4">
                <button
                    onClick={onCancel}
                    className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
                >
                    取消
                </button>
                <button
                    onClick={onSave}
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    检查同步配置
                </button>
            </div>
        </div>
    );
};

export default SettingsPage;